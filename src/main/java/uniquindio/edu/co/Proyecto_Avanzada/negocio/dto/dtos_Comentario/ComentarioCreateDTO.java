package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


/**
 * DTO para CREAR comentarios - Solo campos que el usuario debe proporcionar

 * CAMPOS EXCLUIDOS:
 * - id: Lo genera la base de datos
 * - usuario/alojamiento: Se obtienen de la reserva
 * - fecha: Se asigna automáticamente

 * USO: POST /api/usuario/comentarios (crear comentario)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un comentario")
public class ComentarioCreateDTO {

    @NotNull(message = "La reserva es obligatoria")
    @Schema(description = "ID de la reserva completada", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long reservaId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Schema(description = "Calificación de 1 a 5 estrellas", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "5")
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    @Schema(description = "Comentario opcional", example = "Excelente lugar, muy recomendado", maxLength = 500)
    private String comentario;
}
