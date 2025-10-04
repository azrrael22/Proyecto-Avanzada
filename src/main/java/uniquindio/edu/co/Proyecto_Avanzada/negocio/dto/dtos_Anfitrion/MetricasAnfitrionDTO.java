// Ruta: negocio/dto/dtos_Anfitrion/MetricasAnfitrionDTO.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Anfitrion;

import lombok.Builder;

@Builder
public record MetricasAnfitrionDTO(
        long totalAlojamientos,
        long totalReservasRecibidas,
        Double calificacionPromedio
) {}