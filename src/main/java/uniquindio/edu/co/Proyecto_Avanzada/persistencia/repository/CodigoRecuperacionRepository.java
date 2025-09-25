package uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository;

import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.CodigoRecuperacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos con códigos de recuperación
 */
@Repository
public interface CodigoRecuperacionRepository extends JpaRepository<CodigoRecuperacionEntity, Long> {

    /**
     * Buscar código válido para recuperación
     */
    Optional<CodigoRecuperacionEntity> findByUsuario_EmailAndCodigoAndUsadoFalseAndFechaExpiracionAfter(
            String email, String codigo, LocalDateTime fechaActual);

    /**
     * Buscar último código generado para un usuario
     */
    Optional<CodigoRecuperacionEntity> findTopByUsuario_EmailAndUsadoFalseOrderByFechaCreacionDesc(String email);

    /**
     * Verificar si existe código válido pendiente
     */
    boolean existsByUsuario_EmailAndUsadoFalseAndFechaExpiracionAfter(String email, LocalDateTime fechaActual);

    /**
     * Limpiar códigos expirados
     */
    @Modifying
    @Query("DELETE FROM CodigoRecuperacionEntity c WHERE c.fechaExpiracion < :fecha")
    void deleteExpiredCodes(@Param("fecha") LocalDateTime fecha);

    /**
     * Marcar código como usado
     */
    @Modifying
    @Query("UPDATE CodigoRecuperacionEntity c SET c.usado = true WHERE c.id = :id")
    void markAsUsed(@Param("id") Long id);

    /**
     * Contar códigos activos por usuario
     */
    long countByUsuario_EmailAndUsadoFalseAndFechaExpiracionAfter(String email, LocalDateTime fechaActual);
}