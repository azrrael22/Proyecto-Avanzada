package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.anfitrion;

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
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoUpdateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.AlojamientoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/anfitrion/alojamientos")
@Tag(name = "Alojamientos", description = "Gestión de alojamientos por anfitriones")
@SecurityRequirement(name = "Bearer Authentication")
public class AlojamientoController {

    @Autowired
    private AlojamientoService alojamientoService;

    @PostMapping
    @Operation(summary = "Crear nuevo alojamiento", description = "HU-A001: Crear alojamiento con información completa, servicios e imágenes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alojamiento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (precio <=0, capacidad <=0)"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es anfitrión")
    })
    public ResponseEntity<Map<String, Object>> crearAlojamiento(@RequestBody AlojamientoCreateDTO alojamientoCreateDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // NOTA: El ID del anfitrión se debería extraer del token de autenticación.
            // Por ahora, para probar, usamos un ID fijo.
            Long anfitrionId = 1L;

            var alojamientoCreado = alojamientoService.crearAlojamiento(alojamientoCreateDTO, anfitrionId);

            response.put("message", "Alojamiento creado exitosamente");
            response.put("alojamiento", alojamientoCreado);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Gestionar mis alojamientos", description = "HU-A002: Ver lista de todos mis alojamientos con métricas básicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            // ... (otras respuestas)
    })
    public ResponseEntity<Map<String, Object>> gestionarMisAlojamientos(
            // NOTA: Por ahora ignoraremos los parámetros de paginación y filtro para simplificar.
            // Más adelante se pueden implementar.
            @Parameter(description = "Filtro por estado: ACTIVO, INACTIVO, ELIMINADO")
            @RequestParam(required = false) String estado,
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Como antes, usamos un ID de anfitrión fijo para probar.
            Long anfitrionId = 1L;

            // Llamamos a nuestro nuevo método del servicio.
            List<AlojamientoSummaryDTO> misAlojamientos = alojamientoService.listarAlojamientosPorAnfitrion(anfitrionId);

            response.put("alojamientos", misAlojamientos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{alojamientoId}")
    @Operation(summary = "Editar alojamiento", description = "HU-A002: Actualizar información de alojamiento propio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alojamiento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para editar este alojamiento")
    })
    public ResponseEntity<Map<String, Object>> actualizarAlojamiento(
            @PathVariable Long alojamientoId,
            @RequestBody AlojamientoUpdateDTO updateDTO
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // De nuevo, usamos un ID de anfitrión fijo para probar.
            Long anfitrionId = 1L;

            AlojamientoDTO alojamientoActualizado = alojamientoService.actualizarAlojamiento(alojamientoId, updateDTO, anfitrionId);

            response.put("message", "Alojamiento actualizado exitosamente");
            response.put("alojamiento", alojamientoActualizado);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Manejo de errores específicos
            if (e.getMessage().contains("No tienes permiso")) {
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 403 Prohibido
            }
            if (e.getMessage().contains("no fue encontrado")) {
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 No Encontrado
            }
            // Error genérico
            response.put("error", "Ocurrió un error inesperado: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Activar/Inactivar alojamiento",
            description = "HU-A002: Cambiar estado de alojamiento temporalmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado o no es tuyo"),
            @ApiResponse(responseCode = "409", description = "No se puede inactivar con reservas futuras")
    })
    public ResponseEntity<Map<String, Object>> cambiarEstado(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado: ACTIVO, INACTIVO", required = true)
            @RequestParam String estado,

            @Parameter(description = "Motivo del cambio de estado")
            @RequestParam(required = false) String motivo
    ) {
        Map<String, Object> alojamiento = new HashMap<>();
        alojamiento.put("id", id);
        alojamiento.put("titulo", "Casa Campestre La Calera");
        alojamiento.put("estadoAnterior", "ACTIVO");
        alojamiento.put("nuevoEstado", estado);
        alojamiento.put("fechaCambio", java.time.LocalDateTime.now().toString());
        alojamiento.put("motivo", motivo != null ? motivo : "No especificado");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado actualizado exitosamente");
        response.put("alojamiento", alojamiento);

        if ("INACTIVO".equals(estado)) {
            response.put("efectos", List.of(
                    "El alojamiento no aparecerá en búsquedas",
                    "No se pueden crear nuevas reservas",
                    "Las reservas existentes se mantienen"
            ));
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alojamiento",
            description = "HU-A002: Eliminación lógica si NO hay reservas futuras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alojamiento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado o no es tuyo"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar con reservas futuras activas")
    })
    public ResponseEntity<Map<String, Object>> eliminarAlojamiento(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long id,

            @Parameter(description = "Confirmación de eliminación", required = true)
            @RequestParam boolean confirmar
    ) {
        Map<String, Object> alojamiento = new HashMap<>();
        alojamiento.put("id", id);
        alojamiento.put("titulo", "Casa Campestre La Calera");
        alojamiento.put("estadoAnterior", "ACTIVO");
        alojamiento.put("nuevoEstado", "ELIMINADO");
        alojamiento.put("fechaEliminacion", java.time.LocalDateTime.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Alojamiento eliminado exitosamente");
        response.put("alojamiento", alojamiento);
        response.put("efectos", List.of(
                "El alojamiento no aparecerá en búsquedas",
                "No se pueden crear nuevas reservas",
                "Los datos se conservan para históricos",
                "Esta acción es irreversible"
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/imagenes")
    @Operation(summary = "Subir imágenes del alojamiento",
            description = "Subir entre 1 y 10 imágenes, marcar una como principal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imágenes subidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Formato de imagen inválido o excede límite de 10"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado")
    })
    public ResponseEntity<Map<String, Object>> subirImagenes(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long id,

            @Parameter(description = "URLs de las imágenes separadas por coma", required = true)
            @RequestParam String urlImagenes,

            @Parameter(description = "Índice de la imagen principal (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int imagenPrincipal
    ) {
        List<String> urls = List.of(urlImagenes.split(","));

        Map<String, Object> imagen1 = new HashMap<>();
        imagen1.put("id", 1);
        imagen1.put("url", urls.get(0));
        imagen1.put("esPrincipal", imagenPrincipal == 0);
        imagen1.put("orden", 1);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Imágenes subidas exitosamente");
        response.put("totalImagenes", urls.size());
        response.put("imagenes", List.of(imagen1));
        response.put("imagenPrincipalEstablecida", imagenPrincipal == 0);

        return ResponseEntity.status(201).body(response);
    }
}