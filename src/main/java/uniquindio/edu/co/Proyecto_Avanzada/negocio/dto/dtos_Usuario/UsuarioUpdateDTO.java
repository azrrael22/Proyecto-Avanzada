package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO para ACTUALIZAR usuarios - Solo campos modificables

 * ¿POR QUÉ SEPARAR UPDATE DEL CREATE?
 * 1. SEGURIDAD: NO permite cambiar EMAIL (campo crítico/único)
 * 2. FLEXIBILIDAD: Todos los campos son OPCIONALES (actualización parcial)
 * 3. LÓGICA DE NEGOCIO: Email no se cambia para mantener trazabilidad
 * 4. NO incluye password (se cambia por endpoint separado)

 * CAMPOS EXCLUIDOS:
 * - id: No se puede cambiar
 * - email: Política de negocio - no se permite cambiar
 * - password: Se cambia por endpoint específico
 * - rol/estado: Solo admin puede cambiarlos
 * - fechas: Las maneja el sistema

 * USO: PUT /api/usuario/perfil (actualizar perfil)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar perfil de usuario")
public class UsuarioUpdateDTO {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", maxLength = 100)
    private String nombre;

    @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Formato de teléfono inválido")
    @Schema(description = "Número de teléfono", example = "+57300123456", maxLength = 20)
    private String telefono;

    @Size(max = 500, message = "La URL de foto no puede exceder 500 caracteres")
    @Schema(description = "URL de la foto de perfil", example = "juan.jpg", maxLength = 500)
    private String fotoPerfil;
}