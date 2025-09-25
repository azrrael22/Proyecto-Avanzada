package uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.AccionAuditoria;

/**
 * Entidad que representa la tabla auditoria_usuarios
 */
@Entity
@Table(name = "auditoria_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_administrador", nullable = false)
    private UsuarioEntity administrador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccionAuditoria accion;

    @Column(name = "valor_anterior", length = 50)
    private String valorAnterior;

    @Column(name = "valor_nuevo", length = 50)
    private String valorNuevo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "fecha_accion", nullable = false, updatable = false)
    private LocalDateTime fechaAccion;

    @PrePersist
    protected void onCreate() {
        fechaAccion = LocalDateTime.now();
    }

}