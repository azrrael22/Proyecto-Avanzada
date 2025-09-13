package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * DTO para CREAR respuestas a comentarios
 *
 * USO: POST /api/anfitrion/comentarios/{id}/responder
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para responder un comentario")
public class RespuestaCreateDTO {

    @NotBlank(message = "La respuesta es obligatoria")
    @Size(max = 300, message = "La respuesta no puede exceder 300 caracteres")
    @Schema(description = "Respuesta al comentario", example = "¡Gracias por tu comentario!", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 300)
    private String respuesta;
}
