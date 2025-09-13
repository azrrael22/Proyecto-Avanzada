package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta del anfitrión a un comentario")
public class RespuestaComentarioDTO {

    @Schema(description = "ID de la respuesta", example = "1")
    private Long id;

    @Schema(description = "Nombre del anfitrión", example = "Ana López")
    private String anfitrion;

    @Schema(description = "Texto de la respuesta", example = "¡Gracias Juan! Fue un placer tenerte como huésped")
    private String respuesta;

    @Schema(description = "Fecha de la respuesta", example = "2024-01-18T15:45:00")
    private LocalDateTime fecha;
}
