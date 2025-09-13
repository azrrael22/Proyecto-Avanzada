package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * DTO COMPLETO de usuarios - Para LECTURA/RESPUESTAS

 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Incluye campos auto-generados (ID, fechas) que el cliente necesita ver
 * - NO incluye información sensible como contraseñas
 * - Contiene toda la información disponible del usuario

 * USO: GET /api/usuario/perfil, respuestas de POST/PUT
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa del usuario")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String nombre;

    @Schema(description = "Email del usuario", example = "juan.perez@email.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String email;

    @Schema(description = "Número de teléfono", example = "+57300123456", maxLength = 20)
    private String telefono;

    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "URL de la foto de perfil", example = "juan.jpg", maxLength = 500)
    private String fotoPerfil;

    @Schema(description = "Rol del usuario", example = "USUARIO", accessMode = Schema.AccessMode.READ_ONLY)
    private String rol;

    @Schema(description = "Estado de la cuenta", example = "ACTIVO", accessMode = Schema.AccessMode.READ_ONLY)
    private String estado;

    @Schema(description = "Fecha de registro", example = "2023-01-10T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistro;

    @Schema(description = "Último acceso", example = "2024-01-20T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime ultimoAcceso;
}
