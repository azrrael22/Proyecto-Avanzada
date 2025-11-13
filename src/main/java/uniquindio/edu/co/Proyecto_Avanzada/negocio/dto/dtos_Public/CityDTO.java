package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para ciudades disponibles en la plataforma
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ciudad con alojamientos disponibles")
public class CityDTO {

    @Schema(description = "Nombre de la ciudad", example = "Salento")
    private String ciudad;

    @Schema(description = "Cantidad de alojamientos en la ciudad", example = "15")
    private Long cantidadAlojamientos;
}