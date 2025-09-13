package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Password;


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
@Schema(description = "Cambio voluntario de contraseña")
public class CambiarPasswordDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    @Schema(description = "Contraseña actual", example = "PasswordActual123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "La contraseña debe tener mínimo 8 caracteres, mayúsculas, minúsculas y números")
    @Schema(description = "Nueva contraseña", example = "NuevaPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nuevaPassword;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    @Schema(description = "Confirmación de nueva contraseña", example = "NuevaPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmarPassword;
}
