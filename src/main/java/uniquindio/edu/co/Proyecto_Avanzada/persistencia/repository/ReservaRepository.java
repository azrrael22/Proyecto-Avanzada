package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos con reservas
 */
@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    /**
     * Buscar reservas por usuario
     */
    Page<ReservaEntity> findByUsuario_Id(Long usuarioId, Pageable pageable);

    /**
     * Buscar reservas por usuario y estado
     */
    Page<ReservaEntity> findByUsuario_IdAndEstado(Long usuarioId, EstadoReserva estado, Pageable pageable);

    /**
     * Buscar reservas por anfitrión (todas sus propiedades)
     */
    Page<ReservaEntity> findByAlojamiento_Anfitrion_Id(Long anfitrionId, Pageable pageable);

    /**
     * Buscar reservas por alojamiento específico
     */
    Page<ReservaEntity> findByAlojamiento_Id(Long alojamientoId, Pageable pageable);

    /**
     * Buscar reservas por alojamiento y estado
     */
    Page<ReservaEntity> findByAlojamiento_IdAndEstado(Long alojamientoId, EstadoReserva estado, Pageable pageable);

    /**
     * Reservas en rango de fechas para validar disponibilidad
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN :estados " +
            "AND r.fechaCheckIn <= :checkOut " +
            "AND r.fechaCheckOut >= :checkIn")
    List<ReservaEntity> findConflictingReservations(@Param("alojamientoId") Long alojamientoId,
                                                    @Param("estados") List<EstadoReserva> estados,
                                                    @Param("checkIn") LocalDate checkIn,
                                                    @Param("checkOut") LocalDate checkOut);

    /**
     * Reservas completadas sin comentario
     */
    List<ReservaEntity> findByUsuario_IdAndEstadoAndComentarioIsNull(Long usuarioId, EstadoReserva estado);

    /**
     * Verificar si puede cancelar reserva (48 horas antes)
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReservaEntity r " +
            "WHERE r.id = :reservaId AND r.usuario.id = :usuarioId " +
            "AND r.estado = :estado AND r.fechaCheckIn > :fechaLimite")
    boolean canCancelReservation(@Param("reservaId") Long reservaId,
                                 @Param("usuarioId") Long usuarioId,
                                 @Param("estado") EstadoReserva estado,
                                 @Param("fechaLimite") LocalDate fechaLimite);

    /**
     * Reservas por confirmar (para anfitriones)
     */
    List<ReservaEntity> findByAlojamiento_Anfitrion_IdAndEstadoOrderByFechaReservaDesc(Long anfitrionId,
                                                                                       EstadoReserva estado);

    /**
     * Reservas más recientes
     */
    List<ReservaEntity> findAllByOrderByFechaReservaDesc();

    /**
     * Contar reservas por estado y anfitrión
     */
    long countByAlojamiento_Anfitrion_IdAndEstado(Long anfitrionId, EstadoReserva estado);

    /**
     * Contar reservas por usuario
     */
    long countByUsuario_Id(Long usuarioId);

    /**
     * Calcular ingresos totales por anfitrión
     */
    @Query("SELECT COALESCE(SUM(r.precioTotal), 0) FROM ReservaEntity r " +
            "WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA')")
    BigDecimal calculateTotalIncomeByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    /**
     * Reservas que expiran hoy (para actualizar estados)
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaCheckOut = :fecha AND r.estado = 'CONFIRMADA'")
    List<ReservaEntity> findReservasToComplete(@Param("fecha") LocalDate fecha);
}