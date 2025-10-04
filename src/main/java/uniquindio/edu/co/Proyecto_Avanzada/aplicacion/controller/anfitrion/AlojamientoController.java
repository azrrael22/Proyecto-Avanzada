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
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es anfitrión")
    })
    public ResponseEntity<Map<String, Object>> gestionarMisAlojamientos(
            @Parameter(description = "Filtro por estado: ACTIVO, INACTIVO, ELIMINADO")
            @RequestParam(required = false) String estado,
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> alojamiento1 = new HashMap<>();
        alojamiento1.put("id", 1);
        alojamiento1.put("titulo", "Casa Campestre La Calera");
        alojamiento1.put("ciudad", "La Calera");
        alojamiento1.put("precio", 150000);
        alojamiento1.put("estado", "ACTIVO");
        alojamiento1.put("totalReservas", 15);
        alojamiento1.put("calificacionPromedio", 4.5);
        alojamiento1.put("imagenPrincipal", "casa1.jpg");
        alojamiento1.put("fechaCreacion", "2023-05-15");
        alojamiento1.put("ultimaActualizacion", "2024-01-10");

        Map<String, Object> alojamiento2 = new HashMap<>();
        alojamiento2.put("id", 2);
        alojamiento2.put("titulo", "Apartamento Moderno Centro");
        alojamiento2.put("ciudad", "Bogotá");
        alojamiento2.put("precio", 120000);
        alojamiento2.put("estado", "ACTIVO");
        alojamiento2.put("totalReservas", 8);
        alojamiento2.put("calificacionPromedio", 4.2);
        alojamiento2.put("imagenPrincipal", "apto1.jpg");
        alojamiento2.put("fechaCreacion", "2023-08-20");
        alojamiento2.put("ultimaActualizacion", "2024-01-05");

        Map<String, Object> alojamiento3 = new HashMap<>();
        alojamiento3.put("id", 3);
        alojamiento3.put("titulo", "Finca en Guatapé");
        alojamiento3.put("ciudad", "Guatapé");
        alojamiento3.put("precio", 200000);
        alojamiento3.put("estado", "INACTIVO");
        alojamiento3.put("totalReservas", 3);
        alojamiento3.put("calificacionPromedio", 4.8);
        alojamiento3.put("imagenPrincipal", "finca1.jpg");
        alojamiento3.put("fechaCreacion", "2023-12-01");
        alojamiento3.put("ultimaActualizacion", "2024-01-15");

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalAlojamientos", 3);
        resumen.put("activos", 2);
        resumen.put("inactivos", 1);
        resumen.put("eliminados", 0);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 3);
        paginacion.put("totalPaginas", 1);

        Map<String, Object> response = new HashMap<>();
        response.put("alojamientos", List.of(alojamiento1, alojamiento2, alojamiento3));
        response.put("resumen", resumen);
        response.put("filtros", Map.of("estado", estado));
        response.put("paginacion", paginacion);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar alojamiento",
            description = "HU-A002: Actualizar información de alojamiento propio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alojamiento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado o no es tuyo"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Map<String, Object>> editarAlojamiento(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nuevo título")
            @RequestParam(required = false) String titulo,

            @Parameter(description = "Nueva descripción")
            @RequestParam(required = false) String descripcion,

            @Parameter(description = "Nuevo precio por noche")
            @RequestParam(required = false) Double precio,

            @Parameter(description = "Nueva capacidad máxima")
            @RequestParam(required = false) Integer capacidad,

            @Parameter(description = "Nuevos servicios separados por coma")
            @RequestParam(required = false) String servicios
    ) {
        Map<String, Object> alojamientoActualizado = new HashMap<>();
        alojamientoActualizado.put("id", id);
        alojamientoActualizado.put("titulo", titulo != null ? titulo : "Casa Campestre La Calera");
        alojamientoActualizado.put("descripcion", descripcion != null ? descripcion : "Hermosa casa con vista panorámica");
        alojamientoActualizado.put("precio", precio != null ? precio : 150000);
        alojamientoActualizado.put("capacidad", capacidad != null ? capacidad : 6);
        alojamientoActualizado.put("servicios", servicios != null ? List.of(servicios.split(",")) : List.of("WiFi", "Piscina"));
        alojamientoActualizado.put("fechaActualizacion", java.time.LocalDateTime.now().toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Alojamiento actualizado exitosamente");
        response.put("alojamiento", alojamientoActualizado);

        return ResponseEntity.ok(response);
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