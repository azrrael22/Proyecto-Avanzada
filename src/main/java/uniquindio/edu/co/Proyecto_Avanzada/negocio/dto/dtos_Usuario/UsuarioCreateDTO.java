package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO para CREAR usuarios - Solo campos que el cliente debe proporcionar
 * ¿POR QUÉ SEPARAR CREATE DEL DTO COMPLETO?
 * 1. SEGURIDAD: Cliente NO puede enviar ID (lo genera la BD)
 * 2. SIMPLICIDAD: Solo lo esencial para crear el usuario
 * 3. VALIDACIÓN: Todos los campos obligatorios claramente definidos
 * 4. CLARIDAD: Password se incluye solo en creación, nunca en respuestas

 * CAMPOS EXCLUIDOS:
 * - id: Lo genera la base de datos
 * - rol: Se asigna automáticamente como "USUARIO"
 * - estado: Se asigna automáticamente como "ACTIVO"
 * - fechas: Las maneja el sistema

 * USO: POST /api/auth/registro (crear nuevo usuario)
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar un nuevo usuario")
public class UsuarioCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode =
            Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    @Schema(description = "Email único del usuario", example = "juan.perez@email.com", requiredMode =
            Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "La contraseña debe tener mínimo 8 caracteres, mayúsculas, minúsculas y números")
    @Schema(description = "Contraseña (mínimo 8 caracteres con mayúsculas y números)",
            example = "MiPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Formato de teléfono inválido")
    @Schema(description = "Número de teléfono", example = "+57300123456", maxLength = 20)
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;
}