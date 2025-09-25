package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con imágenes de alojamientos
 */
@Repository
public interface ImagenAlojamientoRepository extends JpaRepository<ImagenAlojamiento, Long> {

    /**
     * Buscar imágenes de un alojamiento ordenadas por orden
     */
    List<ImagenAlojamiento> findByAlojamiento_IdOrderByOrden(Long alojamientoId);

    /**
     * Buscar imagen principal de un alojamiento
     */
    Optional<ImagenAlojamiento> findByAlojamiento_IdAndEsPrincipalTrue(Long alojamientoId);

    /**
     * Contar imágenes de un alojamiento
     */
    long countByAlojamiento_Id(Long alojamientoId);

    /**
     * Verificar si alojamiento tiene imagen principal
     */
    boolean existsByAlojamiento_IdAndEsPrincipalTrue(Long alojamientoId);
}