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
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Anfitrion.MetricasAnfitrionDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.AnfitrionService;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.ReservaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/anfitrion")
@Tag(name = "Anfitrión", description = "Operaciones específicas para anfitriones")
@SecurityRequirement(name = "Bearer Authentication")
public class AnfitrionController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private AnfitrionService anfitrionService;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard del anfitrión",
            description = "Resumen de actividad: alojamientos, reservas, ingresos, métricas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es anfitrión")
    })
    public ResponseEntity<?> dashboard() {
        try {
            // ID del anfitrion fijo para probar
            Long anfitrionId = 1L;

            MetricasAnfitrionDTO metricas = anfitrionService.obtenerMetricas(anfitrionId);

            // Devolvemos directamente el DTO con las métricas para una respuesta limpia
            return new ResponseEntity<>(metricas, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reservas")
    @Operation(summary = "Ver reservas de mis alojamientos",
            description = "HU-A003: Lista de todas las reservas de todos mis alojamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario no es anfitrión")
    })
    public ResponseEntity<Map<String, Object>> verReservasMisAlojamientos(
            @Parameter(description = "Filtrar por alojamiento específico")
            @RequestParam(required = false) Long alojamientoId,
            @Parameter(description = "Filtro por estado: PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA")
            @RequestParam(required = false) String estado,
            @Parameter(description = "Fecha desde (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)")
            @RequestParam(required = false) String fechaHasta,
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ID del anfitrión fijo para probar.
            Long anfitrionId = 1L;

            List<ReservaDTO> reservas = reservaService.listarReservasPorAnfitrion(anfitrionId);

            // Por ahora devolvemos la lista completa. La lógica de filtros se podría añadir después.
            response.put("reservas", reservas);
            // Podríamos añadir también información de resumen y paginación como en el mock original.
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/comentarios")
    @Operation(summary = "Ver comentarios de mis alojamientos",
            description = "HU-A004: Lista de comentarios recibidos en todos mis alojamientos")
    public ResponseEntity<Map<String, Object>> verComentariosMisAlojamientos(
            @Parameter(description = "Filtrar por alojamiento específico")
            @RequestParam(required = false) Long alojamientoId,

            @Parameter(description = "Solo comentarios sin responder")
            @RequestParam(defaultValue = "false") boolean soloSinResponder,

            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> alojamiento1 = new HashMap<>();
        alojamiento1.put("id", 1);
        alojamiento1.put("titulo", "Casa Campestre La Calera");

        Map<String, Object> huesped1 = new HashMap<>();
        huesped1.put("nombre", "María García");
        huesped1.put("iniciales", "MG");

        Map<String, Object> comentario1 = new HashMap<>();
        comentario1.put("id", 1);
        comentario1.put("alojamiento", alojamiento1);
        comentario1.put("huesped", huesped1);
        comentario1.put("calificacion", 5);
        comentario1.put("comentario", "Excelente lugar, muy recomendado");
        comentario1.put("fecha", "2024-01-15T16:30:00");
        comentario1.put("tieneRespuesta", false);
        comentario1.put("puedeResponder", true);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalComentarios", 25);
        resumen.put("sinResponder", 8);
        resumen.put("calificacionPromedio", 4.3);
        resumen.put("ultimaCalificacion", 5);

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("alojamientoId", alojamientoId);
        filtros.put("soloSinResponder", soloSinResponder);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 25);
        paginacion.put("totalPaginas", 3);

        Map<String, Object> response = new HashMap<>();
        response.put("comentarios", List.of(comentario1));
        response.put("resumen", resumen);
        response.put("filtros", filtros);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/comentarios/{comentarioId}/responder")
    @Operation(summary = "Responder a un comentario",
            description = "HU-A004: Responder comentarios de huéspedes (máximo 300 caracteres)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Respuesta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Respuesta muy larga (máximo 300 caracteres)"),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya respondiste este comentario o no es de tu alojamiento")
    })
    public ResponseEntity<Map<String, Object>> responderComentario(
            @Parameter(description = "ID del comentario a responder", required = true)
            @PathVariable Long comentarioId,

            @Parameter(description = "Respuesta al comentario (máximo 300 caracteres)", required = true)
            @RequestParam String respuesta
    ) {
        Map<String, Object> comentarioOriginal = new HashMap<>();
        comentarioOriginal.put("id", comentarioId);
        comentarioOriginal.put("huesped", "María García");
        comentarioOriginal.put("comentario", "Excelente lugar, muy recomendado");
        comentarioOriginal.put("calificacion", 5);

        Map<String, Object> respuestaData = new HashMap<>();
        respuestaData.put("id", 1);
        respuestaData.put("respuesta", respuesta);
        respuestaData.put("fechaRespuesta", java.time.LocalDateTime.now().toString());
        respuestaData.put("anfitrion", "Ana López");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Respuesta enviada exitosamente");
        response.put("comentarioOriginal", comentarioOriginal);
        response.put("respuesta", respuestaData);
        response.put("beneficios", List.of(
                "Mejora la comunicación con huéspedes",
                "Aumenta la confianza de futuros visitantes",
                "Demuestra tu compromiso con el servicio"
        ));

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/metricas")
    @Operation(summary = "Ver métricas de mis alojamientos",
            description = "Estadísticas detalladas de ocupación, ingresos y calificaciones")
    public ResponseEntity<Map<String, Object>> verMetricas(
            @Parameter(description = "Período: mes, trimestre, año", example = "mes")
            @RequestParam(defaultValue = "mes") String periodo,

            @Parameter(description = "Filtrar por alojamiento específico")
            @RequestParam(required = false) Long alojamientoId
    ) {
        Map<String, Object> ingresos = new HashMap<>();
        ingresos.put("totalPeriodo", 2500000);
        ingresos.put("promedioPorReserva", 250000);
        ingresos.put("crecimientoRespectoPeriodoAnterior", 15.5);

        Map<String, Object> ocupacion = new HashMap<>();
        ocupacion.put("porcentajeOcupacion", 75.2);
        ocupacion.put("diasOcupados", 23);
        ocupacion.put("diasDisponibles", 31);
        ocupacion.put("reservasFuturas", 8);

        Map<String, Object> calificaciones = new HashMap<>();
        calificaciones.put("promedioGeneral", 4.4);
        calificaciones.put("totalComentarios", 45);
        calificaciones.put("comentariosEstePeriodo", 12);
        calificaciones.put("tendencia", "MEJORANDO");

        Map<String, Object> alojamientoMetrica1 = new HashMap<>();
        alojamientoMetrica1.put("id", 1);
        alojamientoMetrica1.put("titulo", "Casa Campestre La Calera");
        alojamientoMetrica1.put("ingresosPeriodo", 1500000);
        alojamientoMetrica1.put("ocupacion", 80.5);
        alojamientoMetrica1.put("calificacion", 4.5);
        alojamientoMetrica1.put("totalReservas", 6);

        Map<String, Object> alojamientoMetrica2 = new HashMap<>();
        alojamientoMetrica2.put("id", 2);
        alojamientoMetrica2.put("titulo", "Apartamento Centro");
        alojamientoMetrica2.put("ingresosPeriodo", 1000000);
        alojamientoMetrica2.put("ocupacion", 70.0);
        alojamientoMetrica2.put("calificacion", 4.2);
        alojamientoMetrica2.put("totalReservas", 4);

        Map<String, Object> response = new HashMap<>();
        response.put("periodo", periodo);
        response.put("resumenGeneral", Map.of(
                "ingresos", ingresos,
                "ocupacion", ocupacion,
                "calificaciones", calificaciones
        ));
        response.put("alojamientosDetalle", List.of(alojamientoMetrica1, alojamientoMetrica2));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    @Operation(summary = "Ver perfil como anfitrión",
            description = "Información específica del perfil de anfitrión")
    public ResponseEntity<Map<String, Object>> verPerfilAnfitrion() {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("fechaInicioAnfitrion", "2023-01-15");
        estadisticas.put("totalAlojamientos", 3);
        estadisticas.put("totalReservasRecibidas", 45);
        estadisticas.put("calificacionPromedio", 4.4);
        estadisticas.put("totalHuespedesAtendidos", 120);

        Map<String, Object> reconocimientos = new HashMap<>();
        reconocimientos.put("superAnfitrion", true);
        reconocimientos.put("respuestaRapida", true);
        reconocimientos.put("excelenteLimpieza", false);
        reconocimientos.put("ubicacionEstrategica", true);

        Map<String, Object> response = new HashMap<>();
        response.put("estadisticas", estadisticas);
        response.put("reconocimientos", reconocimientos);

        return ResponseEntity.ok(response);
    }
}