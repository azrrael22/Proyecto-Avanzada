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
@Schema(description = "Verificación de código y cambio de contraseña")
public class VerificarCodigoDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Email del usuario", example = "juan.perez@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[0-9]{6}$", message = "El código debe tener 6 dígitos")
    @Schema(description = "Código de 6 dígitos", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "La contraseña debe tener mínimo 8 caracteres, mayúsculas, minúsculas y números")
    @Schema(description = "Nueva contraseña", example = "NuevaPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nuevaPassword;
}
