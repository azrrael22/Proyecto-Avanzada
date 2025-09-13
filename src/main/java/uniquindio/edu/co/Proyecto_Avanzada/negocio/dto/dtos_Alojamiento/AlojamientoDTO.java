package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.ImagenAlojamientoDTO;


/**
 * DTO COMPLETO de alojamientos - Para LECTURA/RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Incluye información del anfitrión para evitar consultas adicionales
 * - Contiene campos auto-generados (ID, fechas) que el cliente necesita ver
 * - Incluye estadísticas calculadas (calificación, comentarios)
 *
 * USO: GET /api/usuario/alojamientos/{id}, respuestas de POST/PUT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa del alojamiento")

public class AlojamientoDTO {
    
    @Schema(description = "ID único del alojamiento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Título del alojamiento", example = "Casa Campestre La Calera", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String titulo;
    
    @Schema(description = "Descripción detallada", example = "Hermosa casa con vista panorámica", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 2000)
    private String descripcion;
    
    @Schema(description = "Ciudad", example = "La Calera", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String ciudad;
    
    @Schema(description = "Dirección completa", example = "Vereda Alto del Águila, Km 2", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 500)
    private String direccionCompleta;
    
    @Schema(description = "Precio por noche en COP", example = "150000.00", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    private BigDecimal precioPorNoche;
    
    @Schema(description = "Capacidad máxima", example = "6", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    private Integer capacidadMaxima;
    
    @Schema(description = "Tipo de alojamiento", example = "CASA", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoAlojamiento tipo;
    
    @Schema(description = "Servicios disponibles", example = "[\"WiFi\", \"Piscina\", \"Parqueadero\"]")
    private List<String> servicios;
    
    @Schema(description = "Estado actual", example = "ACTIVO", accessMode = Schema.AccessMode.READ_ONLY)
    private EstadoAlojamiento estado;
    
    @Schema(description = "Fecha de creación", example = "2023-05-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Fecha de actualización", example = "2024-01-10T14:20:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;
    
    // Información del anfitrión (denormalizada)
    @Schema(description = "ID del anfitrión", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long anfitrionId;
    
    @Schema(description = "Nombre del anfitrión", example = "María García", accessMode = Schema.AccessMode.READ_ONLY)
    private String anfitrionNombre;
    
    @Schema(description = "Foto del anfitrión", example = "maria.jpg", accessMode = Schema.AccessMode.READ_ONLY)
    private String anfitrionFoto;
    
    // Información de imágenes
    @Schema(description = "Lista de imágenes", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ImagenAlojamientoDTO> imagenes;
    
    // Estadísticas (calculadas)
    @Schema(description = "Calificación promedio", example = "4.5", accessMode = Schema.AccessMode.READ_ONLY)
    private Double calificacionPromedio;
    
    @Schema(description = "Total de comentarios", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer totalComentarios;
    
    @Schema(description = "Total de reservas", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer totalReservas;
}
