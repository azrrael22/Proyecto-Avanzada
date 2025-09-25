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
     * Buscar rol por nombre
     */
    Optional<RolEntity> findByNombreRol(String nombreRol);

    /**
     * Buscar roles activos
     */
    List<RolEntity> findByEsActivoTrue();

    /**
     * Verificar si existe rol por nombre
     */
    boolean existsByNombreRol(String nombreRol);
}