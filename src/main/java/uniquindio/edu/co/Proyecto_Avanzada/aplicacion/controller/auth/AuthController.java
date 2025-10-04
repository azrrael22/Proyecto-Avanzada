package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.UsuarioService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, login y recuperación de contraseña")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService; // ¡Inyectamos nuestro nuevo servicio!

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "HU-V003: Registro de usuario en el sistema")
    @ApiResponses(value = {
            // ... (ApiResponses existentes)
    })
    public ResponseEntity<Map<String, Object>> registro(
            // Para simplificar, asumimos que recibimos el DTO completo
            @RequestBody UsuarioCreateDTO usuarioCreateDTO
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. Llamamos a nuestro servicio para que haga todo el trabajo
            var usuarioRegistrado = usuarioService.registrarUsuario(usuarioCreateDTO);

            // 2. Preparamos una respuesta exitosa
            response.put("message", "Usuario registrado exitosamente");
            response.put("usuario", usuarioRegistrado);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // Código 201

        } catch (Exception e) {
            // 3. Si el servicio lanza un error (ej: email duplicado), lo capturamos
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Código 409
        }
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
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("email", email);
        usuario.put("rol", "USUARIO");
        usuario.put("fechaUltimoAcceso", java.time.LocalDateTime.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.example");
        response.put("tipo", "Bearer");
        response.put("expira", 3600);
        response.put("usuario", usuario);

        return ResponseEntity.ok(response);
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
        Map<String, String> response = new HashMap<>();
        response.put("message", "Código de recuperación enviado al email");
        response.put("expira", "15 minutos");
        response.put("instrucciones", "Revisa tu bandeja de entrada y spam");

        return ResponseEntity.ok(response);
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
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña actualizada exitosamente");
        response.put("email", email);
        response.put("fechaCambio", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}