package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, login y recuperación de contraseña")
public class AuthController {

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "HU-V003: Registro de usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado en el sistema")
    })
    public ResponseEntity<Map<String, Object>> registro(
            @Parameter(description = "Nombre completo del usuario", required = true, example = "Juan Pérez")
            @RequestParam String nombre,

            @Parameter(description = "Email único del usuario", required = true, example = "juan.perez@email.com")
            @RequestParam String email,

            @Parameter(description = "Contraseña (mínimo 8 caracteres con mayúsculas y números)", required = true)
            @RequestParam String password,

            @Parameter(description = "Número de teléfono", example = "+57300123456")
            @RequestParam(required = false) String telefono,

            @Parameter(description = "Fecha de nacimiento en formato YYYY-MM-DD", example = "1990-05-15")
            @RequestParam(required = false) String fechaNacimiento
    ) {
        return ResponseEntity.status(201).body(Map.of(
                "message", "Usuario registrado exitosamente",
                "usuario", Map.of(
                        "id", 1,
                        "nombre", nombre,
                        "email", email,
                        "rol", "USUARIO",
                        "estado", "ACTIVO",
                        "fechaRegistro", java.time.LocalDateTime.now().toString()
                )
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "HU-U001: Autenticación de usuario registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "423", description = "Cuenta desactivada por administrador")
    })
    public ResponseEntity<Map<String, Object>> login(
            @Parameter(description = "Email del usuario registrado", required = true, example = "juan.perez@email.com")
            @RequestParam String email,

            @Parameter(description = "Contraseña del usuario", required = true)
            @RequestParam String password
    ) {
        return ResponseEntity.ok(Map.of(
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                "tipo", "Bearer",
                "expira", 3600,
                "usuario", Map.of(
                        "id", 1,
                        "nombre", "Juan Pérez",
                        "email", email,
                        "rol", "USUARIO",
                        "fechaUltimoAcceso", java.time.LocalDateTime.now().toString()
                )
        ));
    }

    @PostMapping("/recovery")
    @Operation(summary = "Solicitar recuperación de contraseña",
            description = "HU-U002: Envía código de 6 dígitos al email con validez de 15 minutos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código enviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Email no encontrado"),
            @ApiResponse(responseCode = "429", description = "Demasiados intentos, espera antes de solicitar otro código")
    })
    public ResponseEntity<Map<String, String>> recovery(
            @Parameter(description = "Email del usuario registrado", required = true, example = "juan.perez@email.com")
            @RequestParam String email
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Código de recuperación enviado al email",
                "codigo", "123456", // En producción esto NO se retorna
                "expira", "15 minutos",
                "instrucciones", "Revisa tu bandeja de entrada y spam"
        ));
    }

    @PostMapping("/recovery/verificar")
    @Operation(summary = "Verificar código y cambiar contraseña",
            description = "HU-U002: Valida código de 6 dígitos y establece nueva contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Código inválido o expirado"),
            @ApiResponse(responseCode = "422", description = "Nueva contraseña no cumple requisitos de seguridad")
    })
    public ResponseEntity<Map<String, String>> verificarCodigo(
            @Parameter(description = "Email del usuario") @RequestParam String email,
            @Parameter(description = "Código de 6 dígitos recibido por email") @RequestParam String codigo,
            @Parameter(description = "Nueva contraseña que cumple requisitos de seguridad") @RequestParam String nuevaPassword
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Contraseña actualizada exitosamente",
                "email", email,
                "fechaCambio", java.time.LocalDateTime.now().toString()
        ));
    }
}