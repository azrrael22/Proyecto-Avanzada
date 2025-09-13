package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO para CREAR reservas - Solo campos que el usuario debe proporcionar

 * CAMPOS EXCLUIDOS:
 * - id: Lo genera la base de datos
 * - usuario: Se asigna del usuario autenticado
 * - precioTotal: Se calcula automáticamente
 * - estado: Se asigna como "PENDIENTE"
 * - fechas de sistema: Las maneja automáticamente

 * USO: POST /api/usuario/reservas (crear nueva reserva)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva reserva")
public class ReservaCreateDTO {
    @NotNull(message = "El alojamiento es obligatorio")
    @Schema(description = "ID del alojamiento a reservar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long alojamientoId;

    @NotNull(message = "La fecha de check-in es obligatoria")
    @Future(message = "La fecha de check-in debe ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de check-in", example = "2024-02-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaCheckIn;

    @NotNull(message = "La fecha de check-out es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de check-out", example = "2024-02-17", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaCheckOut;

    @NotNull(message = "El número de huéspedes es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 huésped")
    @Schema(description = "Número de huéspedes", example = "4", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    private Integer numHuespedes;
}
