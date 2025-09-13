package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion;

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
@Schema(description = "Datos para iniciar sesión")
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Email del usuario", example = "juan.perez@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña", example = "MiPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}