package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parámetros de búsqueda de alojamientos")
public class BusquedaAlojamientosDTO {

    @Schema(description = "Ciudad", example = "Bogotá")
    private String ciudad;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de check-in", example = "2024-02-15")
    private LocalDate fechaCheckIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de check-out", example = "2024-02-17")
    private LocalDate fechaCheckOut;

    @Schema(description = "Precio mínimo", example = "50000")
    private BigDecimal precioMin;

    @Schema(description = "Precio máximo", example = "200000")
    private BigDecimal precioMax;

    @Schema(description = "Tipo de alojamiento", example = "CASA", allowableValues = {"CASA", "APARTAMENTO", "FINCA"})
    private TipoAlojamiento tipo;

    @Schema(description = "Número de huéspedes", example = "4", minimum = "1")
    private Integer huespedes;

    @Schema(description = "Servicios requeridos", example = "[\"WiFi\", \"Piscina\"]")
    private List<String> servicios;

    @Schema(description = "Ordenar por", example = "precio", allowableValues = {"precio", "calificacion", "fecha"})
    private String ordenarPor = "precio";

    @Schema(description = "Dirección", example = "asc", allowableValues = {"asc", "desc"})
    private String direccion = "asc";
}
