package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.TipoAlojamiento;

/**
 * DTO para CREAR alojamientos - Solo campos que el anfitrión debe proporcionar
 * ¿POR QUÉ SEPARAR CREATE DEL DTO COMPLETO?
 * 1. SEGURIDAD: Cliente NO puede enviar ID (lo genera la BD)
 * 2. SIMPLICIDAD: Solo lo esencial para crear el alojamiento
 * 3. VALIDACIÓN: Todos los campos obligatorios claramente definidos
 * 4. REFERENCIA: Se asigna automáticamente al anfitrión autenticado
 * CAMPOS EXCLUIDOS:
 * - id: Lo genera la base de datos
 * - anfitrion: Se asigna del usuario autenticado
 * - estado: Se asigna automáticamente como "ACTIVO"
 * - fechas: Las maneja el sistema
 * - estadísticas: Se calculan automáticamente

 * USO: POST /api/anfitrion/alojamientos (crear nuevo alojamiento)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo alojamiento")
public class AlojamientoCreateDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Schema(description = "Título del alojamiento", example = "Casa Campestre La Calera", requiredMode =
            Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Descripción detallada del alojamiento",
            example = "Hermosa casa con vista panorámica a los cerros orientales", requiredMode =
            Schema.RequiredMode.REQUIRED, maxLength = 2000)
    private String descripcion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Schema(description = "Ciudad donde se ubica", example = "La Calera", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100)
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    @Schema(description = "Dirección completa", example = "Vereda Alto del Águila, Km 2", requiredMode =
            Schema.RequiredMode.REQUIRED, maxLength = 500)
    private String direccionCompleta;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "1.0", message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio por noche en COP", example = "150000.00", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private BigDecimal precioPorNoche;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Schema(description = "Capacidad máxima de huéspedes", example = "6", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer capacidadMaxima;

    @NotNull(message = "El tipo es obligatorio")
    @Schema(description = "Tipo de alojamiento", example = "CASA", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"CASA", "APARTAMENTO", "FINCA"})
    private TipoAlojamiento tipo;

    @Size(max = 20, message = "No puede tener más de 20 servicios")
    @ArraySchema(
            schema = @Schema(description = "Lista de servicios disponibles", example = "WiFi"),
            maxItems = 20
    )
    private List<String> servicios;

}
