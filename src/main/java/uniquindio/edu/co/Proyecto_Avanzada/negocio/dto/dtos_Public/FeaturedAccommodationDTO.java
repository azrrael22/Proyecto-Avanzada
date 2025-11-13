package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO simplificado para mostrar alojamientos destacados en la landing page
 * Solo incluye información esencial visible para visitantes no autenticados
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información resumida de alojamiento destacado para landing page")
public class FeaturedAccommodationDTO {

    @Schema(description = "ID del alojamiento", example = "1")
    private Long id;

    @Schema(description = "Título del alojamiento", example = "Casa Campestre en Salento")
    private String titulo;

    @Schema(description = "Ciudad", example = "Salento")
    private String ciudad;

    @Schema(description = "Precio por noche en COP", example = "150000.00")
    private BigDecimal precioPorNoche;

    @Schema(description = "URL de la imagen principal", example = "https://cloudinary.com/image1.jpg")
    private String imagenPrincipal;

    @Schema(description = "Calificación promedio", example = "4.5")
    private Double calificacionPromedio;

    @Schema(description = "Indica si está destacado", example = "true")
    private Boolean destacado;
}