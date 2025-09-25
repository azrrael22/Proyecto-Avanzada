package uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.converter.ServiciosConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa la tabla alojamientos
 */
@Entity
@Table(name = "alojamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlojamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alojamiento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anfitrion", nullable = false)
    private UsuarioEntity anfitrion;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(name = "direccion_completa", nullable = false, length = 500)
    private String direccionCompleta;

    @Column(name = "precio_por_noche", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorNoche;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlojamiento tipo;

    @Convert(converter = ServiciosConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> servicios;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoAlojamiento estado = EstadoAlojamiento.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relaciones One-to-Many
    @OneToMany(mappedBy = "alojamiento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImagenAlojamiento> imagenes;

    @OneToMany(mappedBy = "alojamiento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservaEntity> reservas;

    @OneToMany(mappedBy = "alojamiento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ComentarioEntity> comentarios;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}