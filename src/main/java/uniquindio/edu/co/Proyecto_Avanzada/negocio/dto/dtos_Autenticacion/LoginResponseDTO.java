package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación exitosa")
public class LoginResponseDTO {

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.example")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipo;

    @Schema(description = "Tiempo de expiración en segundos", example = "3600")
    private Integer expira;

    @Schema(description = "Información del usuario autenticado")
    private UsuarioDTO usuario;
}
