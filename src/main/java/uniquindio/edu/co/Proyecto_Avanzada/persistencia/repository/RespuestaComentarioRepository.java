package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RespuestaComentarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con respuestas de comentarios
 */
@Repository
public interface RespuestaComentarioRepository extends JpaRepository<RespuestaComentarioEntity, Long> {

    /**
     * Buscar respuesta por comentario específico
     */
    Optional<RespuestaComentarioEntity> findByComentario_Id(Long comentarioId);

    /**
     * Verificar si comentario ya tiene respuesta
     */
    boolean existsByComentario_Id(Long comentarioId);

    /**
     * Buscar todas las respuestas de un anfitrión
     */
    Page<RespuestaComentarioEntity> findByAnfitrion_Id(Long anfitrionId, Pageable pageable);

    /**
     * Buscar respuestas de un anfitrión ordenadas por fecha
     */
    List<RespuestaComentarioEntity> findByAnfitrion_IdOrderByFechaRespuestaDesc(Long anfitrionId);

    /**
     * Verificar si anfitrión puede responder (es dueño del alojamiento)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ComentarioEntity c " +
            "WHERE c.id = :comentarioId " +
            "AND c.alojamiento.anfitrion.id = :anfitrionId")
    boolean canAnfitrionRespond(@Param("comentarioId") Long comentarioId,
                                @Param("anfitrionId") Long anfitrionId);

    /**
     * Buscar comentarios sin respuesta de un anfitrión
     */
    @Query("SELECT c FROM ComentarioEntity c " +
            "WHERE c.alojamiento.anfitrion.id = :anfitrionId " +
            "AND c.id NOT IN (SELECT r.comentario.id FROM RespuestaComentarioEntity r)")
    List<Object> findComentariosSinRespuestaByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    /**
     * Contar respuestas de un anfitrión
     */
    long countByAnfitrion_Id(Long anfitrionId);

    /**
     * Respuestas más recientes del sistema
     */
    List<RespuestaComentarioEntity> findTop10ByOrderByFechaRespuestaDesc();

    /**
     * Buscar respuestas en un rango de fechas
     */
    List<RespuestaComentarioEntity> findByAnfitrion_IdAndFechaRespuestaBetween(
            Long anfitrionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Verificar si respuesta pertenece al anfitrión
     */
    boolean existsByIdAndAnfitrion_Id(Long respuestaId, Long anfitrionId);

    /**
     * Buscar respuestas por texto (contiene)
     */
    List<RespuestaComentarioEntity> findByAnfitrion_IdAndRespuestaContainingIgnoreCase(
            Long anfitrionId, String texto);

    /**
     * Métricas: Promedio de tiempo de respuesta por anfitrión
     */
    @Query("SELECT AVG(DATEDIFF(r.fechaRespuesta, c.fechaComentario)) " +
            "FROM RespuestaComentarioEntity r " +
            "JOIN r.comentario c " +
            "WHERE r.anfitrion.id = :anfitrionId")
    Double findAverageResponseTimeByAnfitrion(@Param("anfitrionId") Long anfitrionId);

    /**
     * Dashboard anfitrión: Respuestas del último mes
     */
    @Query("SELECT COUNT(r) FROM RespuestaComentarioEntity r " +
            "WHERE r.anfitrion.id = :anfitrionId " +
            "AND r.fechaRespuesta >= :fechaDesde")
    Long countRecentResponsesByAnfitrion(@Param("anfitrionId") Long anfitrionId,
                                         @Param("fechaDesde") LocalDateTime fechaDesde);
}