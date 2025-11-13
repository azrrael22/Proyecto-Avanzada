package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ComentarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

/**
 * Repositorio para operaciones de base de datos con comentarios
 */
@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {

    /**
     * Buscar comentarios por usuario
     */
    Page<ComentarioEntity> findByUsuario_Id(Long usuarioId, Pageable pageable);

    /**
     * Buscar comentarios por alojamiento
     */
    Page<ComentarioEntity> findByAlojamiento_Id(Long alojamientoId, Pageable pageable);

    /**
     * Buscar comentarios por alojamiento ordenados por fecha
     */
    Page<ComentarioEntity> findByAlojamiento_IdOrderByFechaComentarioDesc(Long alojamientoId, Pageable pageable);

    /**
     * Buscar comentarios por alojamiento ordenados por calificación
     */
    Page<ComentarioEntity> findByAlojamiento_IdOrderByCalificacionDesc(Long alojamientoId, Pageable pageable);

    /**
     * Buscar comentarios de alojamientos del anfitrión
     */
    Page<ComentarioEntity> findByAlojamiento_Anfitrion_Id(Long anfitrionId, Pageable pageable);

    /**
     * Comentarios sin respuesta del anfitrión
     */
    List<ComentarioEntity> findByAlojamiento_Anfitrion_IdAndRespuestaIsNull(Long anfitrionId);

    /**
     * Verificar si ya existe comentario para una reserva
     */
    boolean existsByReserva_Id(Long reservaId);

    /**
     * Verificar si el comentario pertenece a alojamiento del anfitrión
     */
    boolean existsByIdAndAlojamiento_Anfitrion_Id(Long comentarioId, Long anfitrionId);

    /**
     * Calcular calificación promedio de un alojamiento
     */
    /*
    @Query("SELECT AVG(c.calificacion) FROM ComentarioEntity c WHERE c.alojamiento.id = :alojamientoId")
    Double findAverageCalificacionByAlojamientoId(@Param("alojamientoId") Long alojamientoId);
    */

    /**
     * Contar comentarios por alojamiento
     */
    long countByAlojamiento_Id(Long alojamientoId);

    /**
     * Contar comentarios por anfitrión
     */
    @Query("SELECT COUNT(c) FROM ComentarioEntity c WHERE c.alojamiento.anfitrion.id = :anfitrionId")
    Long countByAnfitrionId(@Param("anfitrionId") Long anfitrionId);

    /**
     * Mejores comentarios (calificación 5) por alojamiento
     */
    List<ComentarioEntity> findByAlojamiento_IdAndCalificacionOrderByFechaComentarioDesc(Long alojamientoId,
                                                                                         Integer calificacion);

    /**
     * Distribución de calificaciones por alojamiento
     */
    @Query("SELECT c.calificacion, COUNT(c) FROM ComentarioEntity c WHERE c.alojamiento.id = :alojamientoId " +
            "GROUP BY c.calificacion ORDER BY c.calificacion DESC")
    List<Object[]> findCalificacionDistribution(@Param("alojamientoId") Long alojamientoId);


    @Query("SELECT AVG(c.calificacion) FROM ComentarioEntity c WHERE c.alojamiento.anfitrion.id = :anfitrionId")
    Double findAverageCalificacionByAnfitrionId(@Param("anfitrionId") Long anfitrionId);

    /**
     * Calcula el promedio de calificación para un alojamiento
     * @param alojamientoId ID del alojamiento
     * @return Promedio de calificación (opcional)
     */
    @Query("SELECT AVG(c.calificacion) FROM ComentarioEntity c WHERE c.alojamiento.id = :alojamientoId")
    Optional<Double> findAverageCalificacionByAlojamientoId(@Param("alojamientoId") Long alojamientoId);
}
