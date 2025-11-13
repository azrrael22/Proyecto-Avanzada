package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;
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
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con reservas
 */
@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    // ========================================
    // MÉTODOS BÁSICOS PARA USUARIOS
    // ========================================

    /**
     * Buscar reservas por usuario
     */
    Page<ReservaEntity> findByUsuario_Id(Long usuarioId, Pageable pageable);

    /**
     * Buscar reservas por usuario y estado
     */
    Page<ReservaEntity> findByUsuario_IdAndEstado(Long usuarioId, EstadoReserva estado, Pageable pageable);

    /**
     * Buscar reservas por usuario en rango de fechas
     */
    Page<ReservaEntity> findByUsuario_IdAndFechaCheckInBetween(Long usuarioId,
                                                               LocalDate fechaInicio,
                                                               LocalDate fechaFin,
                                                               Pageable pageable);

    /**
     * Reservas activas de un usuario (PENDIENTE o CONFIRMADA)
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.usuario.id = :usuarioId " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "ORDER BY r.fechaCheckIn ASC")
    List<ReservaEntity> findActiveReservationsByUsuario(@Param("usuarioId") Long usuarioId);

    // ========================================
    // MÉTODOS PARA ANFITRIONES
    // ========================================

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
     * Reservas por confirmar (para anfitriones)
     */
    List<ReservaEntity> findByAlojamiento_Anfitrion_IdAndEstadoOrderByFechaReservaDesc(Long anfitrionId, EstadoReserva estado);

    /**
     * Buscar reservas por anfitrión con filtros múltiples
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND (:alojamientoId IS NULL OR r.alojamiento.id = :alojamientoId) " +
            "AND (:estado IS NULL OR r.estado = :estado) " +
            "AND (:fechaDesde IS NULL OR r.fechaCheckIn >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR r.fechaCheckOut <= :fechaHasta) " +
            "ORDER BY r.fechaReserva DESC")
    Page<ReservaEntity> findReservasByAnfitrionWithFilters(@Param("anfitrionId") Long anfitrionId,
                                                           @Param("alojamientoId") Long alojamientoId,
                                                           @Param("estado") EstadoReserva estado,
                                                           @Param("fechaDesde") LocalDate fechaDesde,
                                                           @Param("fechaHasta") LocalDate fechaHasta,
                                                           Pageable pageable);

    // ========================================
    // VALIDACIONES DE DISPONIBILIDAD
    // ========================================

    /**
     * Verificar conflictos de fechas para nueva reserva
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN :estados " +
            "AND r.fechaCheckIn < :checkOut " +
            "AND r.fechaCheckOut > :checkIn")
    List<ReservaEntity> findConflictingReservations(@Param("alojamientoId") Long alojamientoId,
                                                    @Param("estados") List<EstadoReserva> estados,
                                                    @Param("checkIn") LocalDate checkIn,
                                                    @Param("checkOut") LocalDate checkOut);

    /**
     * Verificar disponibilidad simple (boolean)
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN false ELSE true END FROM ReservaEntity r " +
            "WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND r.fechaCheckIn < :checkOut " +
            "AND r.fechaCheckOut > :checkIn")
    boolean isAvailable(@Param("alojamientoId") Long alojamientoId,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut);

    // ========================================
    // VALIDACIONES DE CANCELACIÓN
    // ========================================

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
     * Buscar reserva para cancelar (con validaciones)
     */
    // Código Corregido
    @Query("SELECT r FROM ReservaEntity r WHERE r.id = :reservaId " +
            "AND r.usuario.id = :usuarioId " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND r.fechaCheckIn > FUNCTION('DATE_ADD', CURRENT_DATE, 2)") // <-- ESTA ES LA FORMA CORRECTA
    Optional<ReservaEntity> findCancellableReservation(@Param("reservaId") Long reservaId,
                                                       @Param("usuarioId") Long usuarioId);

    // ========================================
    // MÉTODOS PARA COMENTARIOS
    // ========================================

    /**
     * Reservas completadas sin comentario (pueden comentar)
     */
    List<ReservaEntity> findByUsuario_IdAndEstadoAndComentarioIsNull(Long usuarioId, EstadoReserva estado);

    /**
     * Validar que reserva está completada para comentar
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReservaEntity r " +
            "WHERE r.id = :reservaId AND r.usuario.id = :usuarioId " +
            "AND r.estado = 'COMPLETADA' AND r.comentario IS NULL")
    boolean canComment(@Param("reservaId") Long reservaId, @Param("usuarioId") Long usuarioId);

    /**
     * Buscar reserva específica para comentar
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.id = :reservaId " +
            "AND r.usuario.id = :usuarioId " +
            "AND r.estado = 'COMPLETADA' " +
            "AND r.comentario IS NULL")
    Optional<ReservaEntity> findCommentableReservation(@Param("reservaId") Long reservaId,
                                                       @Param("usuarioId") Long usuarioId);

    /**
     * Reservas pendientes de comentario por usuario
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.usuario.id = :usuarioId " +
            "AND r.estado = 'COMPLETADA' " +
            "AND r.comentario IS NULL " +
            "AND r.fechaCheckOut >= :fechaMinima " +
            "ORDER BY r.fechaCheckOut DESC")
    List<ReservaEntity> findPendingCommentReservations(@Param("usuarioId") Long usuarioId,
                                                       @Param("fechaMinima") LocalDate fechaMinima);

    // ========================================
    // VALIDACIÓN DE ELIMINACIÓN DE ALOJAMIENTOS
    // ========================================

    /**
     * Verificar si alojamiento tiene reservas futuras (no se puede eliminar)
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReservaEntity r " +
            "WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND r.fechaCheckIn > CURRENT_DATE")
    boolean hasFutureReservations(@Param("alojamientoId") Long alojamientoId);

    /**
     * Buscar reservas futuras de un alojamiento
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND r.fechaCheckIn > CURRENT_DATE " +
            "ORDER BY r.fechaCheckIn ASC")
    List<ReservaEntity> findFutureReservations(@Param("alojamientoId") Long alojamientoId);

    // ========================================
    // ESTADÍSTICAS Y MÉTRICAS
    // ========================================

    /**
     * Dashboard: Contar reservas por usuario
     */
    long countByUsuario_Id(Long usuarioId);

    /**
     * Dashboard: Contar reservas por estado y anfitrión
     */
    long countByAlojamiento_Anfitrion_IdAndEstado(Long anfitrionId, EstadoReserva estado);

    /**
     * Dashboard: Contar reservas por alojamiento
     */
    long countByAlojamiento_Id(Long alojamientoId);

    /**
     * Métricas: Calcular ingresos totales por anfitrión
     */
    @Query("SELECT COALESCE(SUM(r.precioTotal), 0) FROM ReservaEntity r " +
            "WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA')")
    BigDecimal calculateTotalIncomeByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    /**
     * Métricas: Ingresos por alojamiento específico
     */
    @Query("SELECT COALESCE(SUM(r.precioTotal), 0) FROM ReservaEntity r " +
            "WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA')")
    BigDecimal calculateIncomeByAlojamiento(@Param("alojamientoId") Long alojamientoId);

    /**
     * Métricas: Ingresos en rango de fechas
     */
    @Query("SELECT COALESCE(SUM(r.precioTotal), 0) FROM ReservaEntity r " +
            "WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA') " +
            "AND r.fechaCheckIn BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calculateIncomeByDateRange(@Param("anfitrionId") Long anfitrionId,
                                          @Param("fechaInicio") LocalDate fechaInicio,
                                          @Param("fechaFin") LocalDate fechaFin);

    /**
     * Métricas: Tasa de ocupación por alojamiento
     */
    @Query("SELECT COUNT(r) FROM ReservaEntity r " +
            "WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA') " +
            "AND r.fechaCheckIn BETWEEN :fechaInicio AND :fechaFin")
    Long calculateOccupancyByAlojamiento(@Param("alojamientoId") Long alojamientoId,
                                         @Param("fechaInicio") LocalDate fechaInicio,
                                         @Param("fechaFin") LocalDate fechaFin);

    // ========================================
    // GESTIÓN AUTOMÁTICA DE ESTADOS
    // ========================================

    /**
     * Sistema: Reservas que expiran hoy (para actualizar a COMPLETADA)
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaCheckOut = :fecha " +
            "AND r.estado = 'CONFIRMADA'")
    List<ReservaEntity> findReservasToComplete(@Param("fecha") LocalDate fecha);

    /**
     * Sistema: Reservas que vencen (check-in pasado y aún PENDIENTE)
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaCheckIn < :fecha " +
            "AND r.estado = 'PENDIENTE'")
    List<ReservaEntity> findExpiredPendingReservations(@Param("fecha") LocalDate fecha);

    // ========================================
    // CONSULTAS DE ORDEN Y LISTADOS
    // ========================================

    /**
     * Reservas más recientes del sistema
     */
    List<ReservaEntity> findTop10ByOrderByFechaReservaDesc();

    /**
     * Próximos check-ins por anfitrión
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.estado = 'CONFIRMADA' " +
            "AND r.fechaCheckIn >= CURRENT_DATE " +
            "ORDER BY r.fechaCheckIn ASC")
    List<ReservaEntity> findUpcomingCheckInsByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    /**
     * Reservas del día (check-ins de hoy)
     */
    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaCheckIn = :fecha " +
            "AND r.estado = 'CONFIRMADA'")
    List<ReservaEntity> findTodayCheckIns(@Param("fecha") LocalDate fecha);

    // ========================================
    // REPORTES Y ANÁLISIS
    // ========================================

    /**
     * Reporte: Reservas por mes
     */
    @Query("SELECT YEAR(r.fechaReserva), MONTH(r.fechaReserva), COUNT(r) " +
            "FROM ReservaEntity r WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.fechaReserva >= :fechaDesde " +
            "GROUP BY YEAR(r.fechaReserva), MONTH(r.fechaReserva) " +
            "ORDER BY YEAR(r.fechaReserva) DESC, MONTH(r.fechaReserva) DESC")
    List<Object[]> findMonthlyReservationStats(@Param("anfitrionId") Long anfitrionId,
                                               @Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Análisis: Duración promedio de estadía por alojamiento
     */
    @Query("SELECT AVG(DATEDIFF(r.fechaCheckOut, r.fechaCheckIn)) FROM ReservaEntity r " +
            "WHERE r.alojamiento.id = :alojamientoId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA')")
    Double findAverageStayDuration(@Param("alojamientoId") Long alojamientoId);

    /**
     * Top huéspedes por número de reservas
     */
    @Query("SELECT r.usuario.nombre, COUNT(r) FROM ReservaEntity r " +
            "WHERE r.alojamiento.anfitrion.id = :anfitrionId " +
            "AND r.estado IN ('CONFIRMADA', 'COMPLETADA') " +
            "GROUP BY r.usuario.id, r.usuario.nombre " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> findTopGuestsByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    long countByAlojamiento_Anfitrion_Id(Long anfitrionId);

    /**
     * Cuenta reservas por estado
     * @param estado Estado de la reserva
     * @return Cantidad de reservas
     */
    Long countByEstado(EstadoReserva estado);

}