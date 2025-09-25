package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AuditoriaUsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.AccionAuditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos con auditoría de usuarios
 */
@Repository
public interface AuditoriaUsuarioRepository extends JpaRepository<AuditoriaUsuarioEntity, Long> {

    /**
     * Historial completo de auditoría (más recientes primero)
     */
    Page<AuditoriaUsuarioEntity> findAllByOrderByFechaAccionDesc(Pageable pageable);

    /**
     * Auditoría filtrada por administrador específico
     */
    Page<AuditoriaUsuarioEntity> findByAdministrador_IdOrderByFechaAccionDesc(Long adminId, Pageable pageable);

    /**
     * Auditoría filtrada por tipo de acción
     */
    Page<AuditoriaUsuarioEntity> findByAccionOrderByFechaAccionDesc(AccionAuditoria accion, Pageable pageable);

    /**
     * Auditoría de un usuario específico (historial de cambios)
     */
    List<AuditoriaUsuarioEntity> findByUsuario_IdOrderByFechaAccionDesc(Long usuarioId);

    /**
     * Auditoría filtrada por administrador Y acción
     */
    Page<AuditoriaUsuarioEntity> findByAdministrador_IdAndAccionOrderByFechaAccionDesc(
            Long adminId, AccionAuditoria accion, Pageable pageable);

    /**
     * Auditoría en rango de fechas
     */
    Page<AuditoriaUsuarioEntity> findByFechaAccionBetweenOrderByFechaAccionDesc(
            LocalDateTime fechaDesde, LocalDateTime fechaHasta, Pageable pageable);

    /**
     * Auditoría compleja con múltiples filtros
     */
    @Query("SELECT a FROM AuditoriaUsuarioEntity a WHERE " +
            "(:adminId IS NULL OR a.administrador.id = :adminId) " +
            "AND (:accion IS NULL OR a.accion = :accion) " +
            "AND (:fechaDesde IS NULL OR a.fechaAccion >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR a.fechaAccion <= :fechaHasta) " +
            "ORDER BY a.fechaAccion DESC")
    Page<AuditoriaUsuarioEntity> findWithFilters(@Param("adminId") Long adminId,
                                                 @Param("accion") AccionAuditoria accion,
                                                 @Param("fechaDesde") LocalDateTime fechaDesde,
                                                 @Param("fechaHasta") LocalDateTime fechaHasta,
                                                 Pageable pageable);

    /**
     * Buscar auditoría por justificación (contiene texto)
     */
    List<AuditoriaUsuarioEntity> findByJustificacionContainingIgnoreCaseOrderByFechaAccionDesc(String texto);

    /**
     * Contar acciones por administrador
     */
    long countByAdministrador_Id(Long adminId);

    /**
     * Contar acciones por tipo
     */
    long countByAccion(AccionAuditoria accion);

    /**
     * Estadísticas de acciones agrupadas
     */
    @Query("SELECT a.accion, COUNT(a) FROM AuditoriaUsuarioEntity a " +
            "WHERE a.fechaAccion >= :fechaDesde " +
            "GROUP BY a.accion " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> findActionStatsSince(@Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Administradores más activos
     */
    @Query("SELECT a.administrador.nombre, COUNT(a) FROM AuditoriaUsuarioEntity a " +
            "WHERE a.fechaAccion >= :fechaDesde " +
            "GROUP BY a.administrador.id, a.administrador.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> findMostActiveAdminsSince(@Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Usuarios más modificados
     */
    @Query("SELECT a.usuario.nombre, COUNT(a) FROM AuditoriaUsuarioEntity a " +
            "WHERE a.fechaAccion >= :fechaDesde " +
            "GROUP BY a.usuario.id, a.usuario.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> findMostModifiedUsersSince(@Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Últimas acciones del sistema (dashboard admin)
     */
    List<AuditoriaUsuarioEntity> findTop10ByOrderByFechaAccionDesc();

    /**
     * Acciones de un admin en las últimas 24 horas
     */
    @Query("SELECT COUNT(a) FROM AuditoriaUsuarioEntity a " +
            "WHERE a.administrador.id = :adminId " +
            "AND a.fechaAccion >= :fechaDesde")
    Long countRecentActionsByAdmin(@Param("adminId") Long adminId,
                                   @Param("fechaDesde") LocalDateTime fechaDesde);

    /**
     * Buscar por cambio específico (valor anterior/nuevo)
     */
    @Query("SELECT a FROM AuditoriaUsuarioEntity a WHERE " +
            "(a.valorAnterior = :valor OR a.valorNuevo = :valor) " +
            "ORDER BY a.fechaAccion DESC")
    List<AuditoriaUsuarioEntity> findByValue(@Param("valor") String valor);

    /**
     * Dashboard: Resumen de actividad por día
     */
    @Query("SELECT DATE(a.fechaAccion), COUNT(a) FROM AuditoriaUsuarioEntity a " +
            "WHERE a.fechaAccion >= :fechaDesde " +
            "GROUP BY DATE(a.fechaAccion) " +
            "ORDER BY DATE(a.fechaAccion) DESC")
    List<Object[]> findDailyActivitySince(@Param("fechaDesde") LocalDateTime fechaDesde);
}