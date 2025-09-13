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
@Schema(description = "Información de imagen del alojamiento")
public class ImagenAlojamientoDTO {
    
    @Schema(description = "ID de la imagen", example = "1")
    private Long id;
    
    @Schema(description = "URL de la imagen", example = "casa1_principal.jpg")
    private String url;
    
    @Schema(description = "Es imagen principal", example = "true")
    private Boolean esPrincipal;
    
    @Schema(description = "Orden de visualización", example = "1")
    private Integer orden;
    
    @Schema(description = "Fecha de subida", example = "2023-05-15T10:30:00")
    private LocalDateTime fechaSubida;
}
