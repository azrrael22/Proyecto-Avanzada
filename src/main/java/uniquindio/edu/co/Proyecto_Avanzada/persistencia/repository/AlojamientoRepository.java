package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import java.util.List;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Repositorio para operaciones de base de datos con alojamientos
 */
@Repository
public interface AlojamientoRepository extends JpaRepository<AlojamientoEntity, Long> {

    /**
     * Buscar alojamientos por anfitrión
     */
    List<AlojamientoEntity> findByAnfitrion_Id(Long anfitrionId);

    /**
     * Busca todos los alojamientos de un anfitrión específico que NO tengan un estado determinado.
     */
    List<AlojamientoEntity> findByAnfitrion_IdAndEstadoNot(Long anfitrionId, EstadoAlojamiento estado);

    /**
     * Buscar alojamientos por anfitrión con paginación
     */
    Page<AlojamientoEntity> findByAnfitrion_Id(Long anfitrionId, Pageable pageable);

    /**
     * Buscar alojamientos por anfitrión excluyendo eliminados
     */
    Page<AlojamientoEntity> findByAnfitrion_IdAndEstadoNot(Long anfitrionId, EstadoAlojamiento estado,
                                                           Pageable pageable);

    /**
     * Buscar alojamientos por ciudad (búsqueda pública)
     */
    Page<AlojamientoEntity> findByEstadoAndCiudadContainingIgnoreCase(EstadoAlojamiento estado, String ciudad,
                                                                      Pageable pageable);

    /**
     * Buscar alojamientos por tipo
     */
    Page<AlojamientoEntity> findByEstadoAndTipo(EstadoAlojamiento estado, TipoAlojamiento tipo, Pageable pageable);

    /**
     * Buscar alojamientos por rango de precios
     */
    Page<AlojamientoEntity> findByEstadoAndPrecioPorNocheBetween(EstadoAlojamiento estado, BigDecimal precioMin,
                                                                 BigDecimal precioMax, Pageable pageable);

    /**
     * Buscar alojamientos por capacidad mínima
     */
    Page<AlojamientoEntity> findByEstadoAndCapacidadMaximaGreaterThanEqual(EstadoAlojamiento estado,
                                                                           Integer capacidad, Pageable pageable);

    /**
     * Vista previa pública limitada (6 alojamientos destacados)
     */
    List<AlojamientoEntity> findTop6ByEstadoOrderByFechaCreacionDesc(EstadoAlojamiento estado);

    /**
     * Alojamientos ordenados por precio ascendente
     */
    List<AlojamientoEntity> findByEstadoOrderByPrecioPorNocheAsc(EstadoAlojamiento estado);

    /**
     * Alojamientos ordenados por calificación descendente
     */
    @Query("SELECT a FROM AlojamientoEntity a LEFT JOIN a.comentarios c WHERE a.estado = :estado GROUP BY a.id " +
            "ORDER BY AVG(c.calificacion) DESC NULLS LAST")
    List<AlojamientoEntity> findByEstadoOrderByCalificacionDesc(@Param("estado") EstadoAlojamiento estado);

    /**
     * Verificar si el alojamiento pertenece al anfitrión
     */
    boolean existsByAnfitrion_IdAndId(Long anfitrionId, Long alojamientoId);

    /**
     * Contar alojamientos por anfitrión
     */
    long countByAnfitrion_Id(Long anfitrionId);

    /**
     * Contar alojamientos por estado
     */
    long countByEstado(EstadoAlojamiento estado);

    /**
     * Calcular calificación promedio de un alojamiento
     */
    @Query("SELECT AVG(c.calificacion) FROM ComentarioEntity c WHERE c.alojamiento.id = :alojamientoId")
    Double findAverageCalificacionByAlojamientoId(@Param("alojamientoId") Long alojamientoId);

    /**
     * Alojamientos con reservas futuras (no se pueden eliminar)
     */
    @Query("SELECT DISTINCT a FROM AlojamientoEntity a JOIN a.reservas r WHERE a.id = :alojamientoId AND r.estado " +
            "IN ('PENDIENTE', 'CONFIRMADA') AND r.fechaCheckIn > CURRENT_DATE")
    List<AlojamientoEntity> findAlojamientosWithFutureReservations(@Param("alojamientoId") Long alojamientoId);

    /**
     * Búsqueda compleja con múltiples filtros
     */
    @Query("SELECT a FROM AlojamientoEntity a WHERE a.estado = 'ACTIVO' " +
            "AND (:ciudad IS NULL OR LOWER(a.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
            "AND (:tipo IS NULL OR a.tipo = :tipo) " +
            "AND (:precioMin IS NULL OR a.precioPorNoche >= :precioMin) " +
            "AND (:precioMax IS NULL OR a.precioPorNoche <= :precioMax) " +
            "AND (:capacidad IS NULL OR a.capacidadMaxima >= :capacidad)")
    Page<AlojamientoEntity> findWithFilters(@Param("ciudad") String ciudad,
                                            @Param("tipo") TipoAlojamiento tipo,
                                            @Param("precioMin") BigDecimal precioMin,
                                            @Param("precioMax") BigDecimal precioMax,
                                            @Param("capacidad") Integer capacidad,
                                            Pageable pageable);

    @Query("SELECT a FROM AlojamientoEntity a WHERE a.estado = 'ACTIVO' " +
            "AND (:ciudad IS NULL OR LOWER(a.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
            "AND (:precioMin IS NULL OR a.precioPorNoche >= :precioMin) " +
            "AND (:precioMax IS NULL OR a.precioPorNoche <= :precioMax) " +
            "AND (:tipo IS NULL OR a.tipo = :tipo) " +
            // Subconsulta para verificar disponibilidad de fechas
            "AND NOT EXISTS (SELECT r FROM ReservaEntity r WHERE r.alojamiento.id = a.id " +
            "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND (r.fechaCheckIn < :fechaCheckOut AND r.fechaCheckOut > :fechaCheckIn))")
    Page<AlojamientoEntity> buscarAlojamientosDisponibles(
            @Param("ciudad") String ciudad,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("tipo") TipoAlojamiento tipo,
            @Param("fechaCheckIn") LocalDate fechaCheckIn,
            @Param("fechaCheckOut") LocalDate fechaCheckOut,
            Pageable pageable
    );
}