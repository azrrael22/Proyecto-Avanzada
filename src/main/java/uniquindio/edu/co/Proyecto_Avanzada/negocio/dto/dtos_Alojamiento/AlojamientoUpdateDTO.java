package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para ACTUALIZAR alojamientos - Solo campos modificables
 *
 * ¿POR QUÉ SEPARAR UPDATE DEL CREATE?
 * 1. FLEXIBILIDAD: Todos los campos son OPCIONALES (actualización parcial)
 * 2. LÓGICA DE NEGOCIO: NO permite cambiar anfitrión después de crear
 * 3. SEGURIDAD: Evita reasignaciones no autorizadas de alojamientos
 *
 * CAMPOS EXCLUIDOS:
 * - id: No se puede cambiar
 * - anfitrion: No se puede reasignar un alojamiento
 * - estado: Se cambia por endpoint específico
 * - fechas: Las maneja el sistema
 *
 * USO: PUT /api/anfitrion/alojamientos/{id} (actualizar alojamiento)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un alojamiento existente")

public class AlojamientoUpdateDTO {
    
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Schema(description = "Título del alojamiento", example = "Casa Campestre La Calera", maxLength = 200)
    private String titulo;
    
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Descripción detallada", example = "Hermosa casa con vista panorámica", maxLength = 2000)
    private String descripcion;
    
    @DecimalMin(value = "1.0", message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio por noche en COP", example = "150000.00", minimum = "1")
    private BigDecimal precioPorNoche;
    
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Schema(description = "Capacidad máxima", example = "6", minimum = "1")
    private Integer capacidadMaxima;
    
    @Schema(description = "Servicios disponibles", example = "[\"WiFi\", \"Piscina\", \"Parqueadero\"]")
    private List<String> servicios;
}
