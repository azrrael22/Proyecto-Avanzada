package uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla codigos_recuperacion
 */
@Entity
@Table(name = "codigos_recuperacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoRecuperacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Builder.Default
    private Boolean usado = false;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        // El trigger de la BD se encarga de establecer la fecha de expiración
        // pero podemos hacerlo aquí también como respaldo
        if (fechaExpiracion == null) {
            fechaExpiracion = fechaCreacion.plusMinutes(15);
        }
    }
}