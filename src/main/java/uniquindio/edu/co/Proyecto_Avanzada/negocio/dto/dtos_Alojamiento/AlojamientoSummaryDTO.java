package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;

/**
 * DTO RESUMIDO de alojamientos - Para LISTAS/BÚSQUEDAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Optimizar respuestas en listas (menos datos por item)
 * - Incluir solo información esencial para mostrar en tarjetas
 * - Mejor performance al evitar datos innecesarios
 *
 * USO: GET /api/usuario/alojamientos (búsquedas y listas)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información resumida del alojamiento para listas")

public class AlojamientoSummaryDTO {
    
    @Schema(description = "ID del alojamiento", example = "1")
    private Long id;
    
    @Schema(description = "Título", example = "Casa Campestre La Calera")
    private String titulo;
    
    @Schema(description = "Ciudad", example = "La Calera")
    private String ciudad;
    
    @Schema(description = "Precio por noche", example = "150000.00")
    private BigDecimal precioPorNoche;
    
    @Schema(description = "Capacidad máxima", example = "6")
    private Integer capacidadMaxima;
    
    @Schema(description = "Tipo", example = "CASA")
    private TipoAlojamiento tipo;
    
    @Schema(description = "Imagen principal", example = "casa1.jpg")
    private String imagenPrincipal;
    
    @Schema(description = "Calificación promedio", example = "4.5")
    private Double calificacionPromedio;
    
    @Schema(description = "Estado", example = "ACTIVO")
    private EstadoAlojamiento estado;
}
