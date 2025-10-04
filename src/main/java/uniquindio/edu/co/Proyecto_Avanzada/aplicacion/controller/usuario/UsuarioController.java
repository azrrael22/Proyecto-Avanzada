package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.BusquedaAlojamientosDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.AlojamientoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuario", description = "Operaciones para usuarios autenticados")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    // Al principio de la clase UsuarioController.java, añade esta inyección:
    @Autowired
    private AlojamientoService alojamientoService;

    @GetMapping("/alojamientos")
    @Operation(summary = "Buscar alojamientos", description = "HU-U003: Búsqueda de alojamientos con filtros...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos")
    })
    public ResponseEntity<List<AlojamientoSummaryDTO>> buscarAlojamientos(
            // Spring Boot mapeará automáticamente los parámetros de la URL a este objeto DTO
            @ModelAttribute BusquedaAlojamientosDTO filtros
    ) {
        try {
            List<AlojamientoSummaryDTO> resultados = alojamientoService.buscarAlojamientosDisponibles(filtros);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/alojamientos/{id}")
    @Operation(summary = "Ver detalles completos de alojamiento",
            description = "HU-U004: Galería, descripción, servicios, mapa, calendario, comentarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> detalleAlojamiento(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long id
    ) {
        Map<String, Object> anfitrion = new HashMap<>();
        anfitrion.put("nombre", "María García");
        anfitrion.put("foto", "maria.jpg");
        anfitrion.put("fechaRegistro", "2023-01-15");

        Map<String, Object> alojamiento = new HashMap<>();
        alojamiento.put("id", id);
        alojamiento.put("titulo", "Casa Campestre La Calera");
        alojamiento.put("descripcion", "Hermosa casa con vista panorámica a los cerros orientales");
        alojamiento.put("anfitrion", anfitrion);
        alojamiento.put("capacidad", 6);
        alojamiento.put("precio", 150000);
        alojamiento.put("tipo", "CASA");
        alojamiento.put("ciudad", "La Calera");
        alojamiento.put("direccion", "Vereda Alto del Águila, Km 2");

        Map<String, Object> imagen1 = new HashMap<>();
        imagen1.put("url", "casa1_1.jpg");
        imagen1.put("esPrincipal", true);
        imagen1.put("orden", 1);

        Map<String, Object> imagen2 = new HashMap<>();
        imagen2.put("url", "casa1_2.jpg");
        imagen2.put("esPrincipal", false);
        imagen2.put("orden", 2);

        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("latitud", 4.7315);
        ubicacion.put("longitud", -74.0431);
        ubicacion.put("descripcion", "A 30 minutos de Bogotá");

        Map<String, Object> comentario1 = new HashMap<>();
        comentario1.put("usuario", "Juan Pérez");
        comentario1.put("calificacion", 5);
        comentario1.put("comentario", "Excelente lugar, muy recomendado");
        comentario1.put("fecha", "2024-01-15");
        comentario1.put("respuesta", "Gracias por tu comentario, Juan!");

        Map<String, Object> comentario2 = new HashMap<>();
        comentario2.put("usuario", "Ana Rodríguez");
        comentario2.put("calificacion", 4);
        comentario2.put("comentario", "Muy cómodo y limpio");
        comentario2.put("fecha", "2024-01-10");
        comentario2.put("respuesta", null);

        Map<String, Object> comentarios = new HashMap<>();
        comentarios.put("promedio", 4.5);
        comentarios.put("total", 12);
        comentarios.put("lista", List.of(comentario1, comentario2));

        Map<String, Object> response = new HashMap<>();
        response.put("alojamiento", alojamiento);
        response.put("galeria", List.of(imagen1, imagen2));
        response.put("servicios", List.of("WiFi", "Piscina", "Parqueadero", "Asador", "Chimenea", "Jardín"));
        response.put("ubicacion", ubicacion);
        response.put("disponibilidad", List.of("2024-02-15", "2024-02-16", "2024-02-17", "2024-02-20"));
        response.put("comentarios", comentarios);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/convertir-anfitrion")
    @Operation(summary = "Convertirse en anfitrión",
            description = "HU-U011: Cambio automático de rol de USUARIO a ANFITRION")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Usuario ya es anfitrión o no cumple requisitos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> convertirAnfitrion() {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("rolAnterior", "USUARIO");
        usuario.put("nuevoRol", "ANFITRION");
        usuario.put("fechaCambio", java.time.LocalDateTime.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Felicidades! Ahora eres un anfitrión");
        response.put("usuario", usuario);
        response.put("beneficios", List.of(
                "Puedes crear y gestionar alojamientos",
                "Recibir reservas de huéspedes",
                "Generar ingresos con tus propiedades",
                "Acceso a métricas y estadísticas"
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard de usuario",
            description = "Resumen de actividad del usuario: reservas recientes, favoritos, etc.")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("reservasActivas", 2);
        resumen.put("reservasCompletadas", 5);
        resumen.put("comentariosPendientes", 1);
        resumen.put("favoritos", 3);

        Map<String, Object> reserva1 = new HashMap<>();
        reserva1.put("id", 1);
        reserva1.put("alojamiento", "Casa Campestre");
        reserva1.put("estado", "CONFIRMADA");
        reserva1.put("fechas", "2024-02-15 a 2024-02-17");

        Map<String, Object> reserva2 = new HashMap<>();
        reserva2.put("id", 2);
        reserva2.put("alojamiento", "Apartamento Centro");
        reserva2.put("estado", "PENDIENTE");
        reserva2.put("fechas", "2024-03-01 a 2024-03-03");

        Map<String, Object> recomendacion1 = new HashMap<>();
        recomendacion1.put("id", 10);
        recomendacion1.put("titulo", "Villa en Melgar");
        recomendacion1.put("precio", 180000);
        recomendacion1.put("calificacion", 4.6);

        Map<String, Object> recomendacion2 = new HashMap<>();
        recomendacion2.put("id", 11);
        recomendacion2.put("titulo", "Casa en Girardot");
        recomendacion2.put("precio", 140000);
        recomendacion2.put("calificacion", 4.3);

        Map<String, Object> response = new HashMap<>();
        response.put("resumen", resumen);
        response.put("reservasRecientes", List.of(reserva1, reserva2));
        response.put("recomendaciones", List.of(recomendacion1, recomendacion2));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    @Operation(summary = "Ver perfil de usuario",
            description = "HU-U009: Información del perfil propio")
    public ResponseEntity<Map<String, Object>> verPerfil() {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1);
        usuario.put("nombre", "Juan Pérez");
        usuario.put("email", "juan.perez@email.com");
        usuario.put("telefono", "+57300123456");
        usuario.put("fechaNacimiento", "1990-05-15");
        usuario.put("fechaRegistro", "2023-01-10");
        usuario.put("rol", "USUARIO");
        usuario.put("fotoPerfil", "juan.jpg");

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/perfil")
    @Operation(summary = "Editar perfil de usuario",
            description = "HU-U009: Actualizar información personal (excepto email)")
    public ResponseEntity<Map<String, Object>> editarPerfil(
            @Parameter(description = "Nombre completo") @RequestParam(required = false) String nombre,
            @Parameter(description = "Teléfono") @RequestParam(required = false) String telefono,
            @Parameter(description = "URL de foto de perfil") @RequestParam(required = false) String fotoPerfil
    ) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", 1);
        usuario.put("nombre", nombre != null ? nombre : "Juan Pérez");
        usuario.put("telefono", telefono != null ? telefono : "+57300123456");
        usuario.put("fotoPerfil", fotoPerfil != null ? fotoPerfil : "juan.jpg");
        usuario.put("fechaActualizacion", java.time.LocalDateTime.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil actualizado exitosamente");
        response.put("usuario", usuario);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cambiar-password")
    @Operation(summary = "Cambiar contraseña",
            description = "HU-U010: Cambiar contraseña voluntariamente")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @Parameter(description = "Contraseña actual") @RequestParam String passwordActual,
            @Parameter(description = "Nueva contraseña") @RequestParam String nuevaPassword,
            @Parameter(description = "Confirmar nueva contraseña") @RequestParam String confirmarPassword
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contraseña actualizada exitosamente");
        response.put("fechaCambio", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}