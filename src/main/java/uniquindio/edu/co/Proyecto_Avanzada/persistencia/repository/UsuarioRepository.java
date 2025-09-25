package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoUsuario;
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
 * Repositorio para operaciones de base de datos con usuarios
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Buscar usuario por email (para login)
     */
    Optional<UsuarioEntity> findByEmail(String email);

    /**
     * Verificar si existe un usuario con el email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar usuarios por nombre (contiene texto)
     */
    List<UsuarioEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar usuarios por rol
     */
    Page<UsuarioEntity> findByRol_NombreRol(String nombreRol, Pageable pageable);

    /**
     * Buscar usuarios por estado
     */
    Page<UsuarioEntity> findByEstado(EstadoUsuario estado, Pageable pageable);

    /**
     * Buscar usuarios por rol y estado
     */
    Page<UsuarioEntity> findByRol_NombreRolAndEstado(String nombreRol, EstadoUsuario estado, Pageable pageable);

    /**
     * Buscar usuarios registrados en un rango de fechas
     */
    List<UsuarioEntity> findByFechaRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Buscar usuarios más recientes
     */
    List<UsuarioEntity> findAllByOrderByFechaRegistroDesc();

    /**
     * Contar usuarios por estado
     */
    long countByEstado(EstadoUsuario estado);

    /**
     * Contar usuarios por rol
     */
    long countByRol_NombreRol(String nombreRol);

    /**
     * Usuarios activos con reservas
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.estado = 'ACTIVO' AND SIZE(u.reservas) > 0")
    List<UsuarioEntity> findActiveUsersWithReservations();

    /**
     * Usuarios que son anfitriones con alojamientos
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol.nombreRol = 'ANFITRION' AND SIZE(u.alojamientos) > 0")
    List<UsuarioEntity> findAnfitrionesWithAlojamientos();

    /**
     * Contar total de reservas por usuario
     */
    @Query("SELECT COUNT(r) FROM ReservaEntity r WHERE r.usuario.id = :usuarioId")
    Long countReservasByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Validar perfil completo para ser anfitrión
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UsuarioEntity u " +
            "WHERE u.id = :usuarioId AND u.nombre IS NOT NULL AND u.telefono IS NOT NULL")
    boolean hasCompleteProfile(@Param("usuarioId") Long usuarioId);
}