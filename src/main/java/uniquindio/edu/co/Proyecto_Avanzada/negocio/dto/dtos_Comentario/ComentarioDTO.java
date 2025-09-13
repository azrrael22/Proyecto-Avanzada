package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.RespuestaComentarioDTO;
import java.time.LocalDateTime;


/**
 * DTO COMPLETO de comentarios - Para LECTURA/RESPUESTAS
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa del comentario")
public class ComentarioDTO {

    @Schema(description = "ID del comentario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    // Información del usuario (denormalizada)
    @Schema(description = "Nombre del usuario", example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "Iniciales del usuario", example = "JP", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioIniciales;

    @Schema(description = "Foto del usuario", example = "juan.jpg", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioFoto;

    // Información del alojamiento (denormalizada)
    @Schema(description = "ID del alojamiento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long alojamientoId;

    @Schema(description = "Título del alojamiento", example = "Casa Campestre La Calera", accessMode
            = Schema.AccessMode.READ_ONLY)
    private String alojamientoTitulo;

    @Schema(description = "Imagen del alojamiento", example = "casa1.jpg", accessMode = Schema.AccessMode.READ_ONLY)
    private String alojamientoImagen;

    // Datos del comentario
    @Schema(description = "Calificación", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer calificacion;

    @Schema(description = "Texto del comentario", example = "Excelente lugar, muy recomendado")
    private String comentario;

    @Schema(description = "Fecha del comentario", example = "2024-01-18T16:30:00", accessMode
            = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecha;

    // Respuesta del anfitrión
    @Schema(description = "Respuesta del anfitrión", accessMode = Schema.AccessMode.READ_ONLY)
    private RespuestaComentarioDTO respuesta;

    // Información de la estadía
    @Schema(description = "Fechas de la estadía", example = "2024-01-15 a 2024-01-17", accessMode
            = Schema.AccessMode.READ_ONLY)
    private String fechasEstadia;
}
