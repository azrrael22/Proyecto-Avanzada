package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginRequestDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginResponseDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.auth.AuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para autenticación (Login y Registro)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint de Login
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con email y contraseña, retornando un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Llamar al servicio de autenticación
            LoginResponseDTO loginResponse = authService.login(loginRequest);

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            response.put("error", "Credenciales inválidas");
            response.put("message", e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * Endpoint de Registro
     */
    @PostMapping("/register")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> register(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Registrar el usuario
            UsuarioDTO nuevoUsuario = authService.register(usuarioCreateDTO);

            response.put("message", "Usuario registrado exitosamente");
            response.put("usuario", nuevoUsuario);

            return ResponseEntity.status(201).body(response);

        } catch (Exception e) {
            response.put("error", "Error al registrar usuario");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * Endpoint para verificar si el token es válido (OPCIONAL)
     */
    @GetMapping("/validate")
    @Operation(summary = "Validar token JWT", description = "Verifica si el token JWT es válido")
    public ResponseEntity<?> validateToken() {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("message", "Token válido");
        return ResponseEntity.ok(response);
    }
}
