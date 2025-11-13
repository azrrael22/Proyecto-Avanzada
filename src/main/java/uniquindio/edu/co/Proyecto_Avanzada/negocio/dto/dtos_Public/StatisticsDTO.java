package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para estadísticas generales de la plataforma
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas generales de la plataforma")
public class StatisticsDTO {

    @Schema(description = "Total de alojamientos disponibles", example = "150")
    private Long totalAlojamientos;

    @Schema(description = "Total de usuarios registrados", example = "320")
    private Long totalUsuarios;

    @Schema(description = "Total de ciudades con alojamientos", example = "25")
    private Long totalCiudades;

    @Schema(description = "Total de reservas completadas", example = "450")
    private Long totalReservas;
}