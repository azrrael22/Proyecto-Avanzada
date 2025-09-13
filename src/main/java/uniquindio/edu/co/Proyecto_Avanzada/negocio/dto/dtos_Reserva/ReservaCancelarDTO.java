package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cancelar reserva")
public class ReservaCancelarDTO {

    @Size(max = 500, message = "El motivo no puede exceder 500 caracteres")
    @Schema(description = "Motivo de la cancelación", example = "Cambio de planes", maxLength = 500)
    private String motivo;
}