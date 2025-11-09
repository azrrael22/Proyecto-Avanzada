package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con roles
 */
@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    /**
     * Busca un rol por su nombre
     * @param nombreRol Nombre del rol (ej: "USUARIO", "ANFITRION", "ADMINISTRADOR")
     * @return Optional con el rol si existe
     */
    Optional<RolEntity> findByNombreRol(String nombreRol);

    /**
     * Buscar roles activos
     */
    List<RolEntity> findByEsActivoTrue();

    /**
     * Verifica si existe un rol con el nombre dado
     * @param nombreRol Nombre del rol
     * @return true si existe, false si no
     */
    boolean existsByNombreRol(String nombreRol);
}