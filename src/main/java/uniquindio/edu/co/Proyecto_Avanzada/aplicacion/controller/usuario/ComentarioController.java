package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario/comentarios")
@Tag(name = "Comentarios Usuario", description = "Comentarios y calificaciones de huéspedes")
@SecurityRequirement(name = "Bearer Authentication")
public class ComentarioController {

    @PostMapping
    @Operation(summary = "Dejar comentario y calificación",
            description = "HU-U008: Comentar y calificar reserva completada (solo una vez por reserva)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Calificación inválida (debe ser 1-5) o comentario muy largo"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya comentaste esta reserva o reserva no completada"),
            @ApiResponse(responseCode = "422", description = "Solo puedes comentar reservas completadas")
    })
    public ResponseEntity<Map<String, Object>> dejarComentario(
            @Parameter(description = "ID de la reserva completada", required = true, example = "1")
            @RequestParam Long reservaId,

            @Parameter(description = "Calificación de 1 a 5 estrellas", required = true, example = "5")
            @RequestParam Integer calificacion,

            @Parameter(description = "Comentario opcional (máximo 500 caracteres)", example = "Excelente lugar, muy recomendado")
            @RequestParam(required = false) String comentario
    ) {
        Map<String, Object> reserva = new HashMap<>();
        reserva.put("id", reservaId);
        reserva.put("alojamiento", "Casa Campestre La Calera");
        reserva.put("fechaEstadia", "2024-01-15 a 2024-01-17");

        Map<String, Object> comentarioData = new HashMap<>();
        comentarioData.put("id", 1);
        comentarioData.put("reserva", reserva);
        comentarioData.put("calificacion", calificacion);
        comentarioData.put("comentario", comentario != null ? comentario : "");
        comentarioData.put("fecha", java.time.LocalDateTime.now().toString());
        comentarioData.put("usuario", "Juan Pérez");

        Map<String, Object> impacto = new HashMap<>();
        impacto.put("nuevoPromedio", 4.3);
        impacto.put("totalComentarios", 8);
        impacto.put("ayudasOtrosViajeros", true);

        Map<String, Object> response = new HashMap<>();
        response.put("comentario", comentarioData);
        response.put("message", "¡Gracias por tu comentario!");
        response.put("impacto", impacto);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/mis-comentarios")
    @Operation(summary = "Ver mis comentarios realizados",
            description = "Lista de todos los comentarios que he hecho en mis reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> misComentarios(
            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> alojamiento1 = new HashMap<>();
        alojamiento1.put("id", 1);
        alojamiento1.put("titulo", "Casa Campestre La Calera");
        alojamiento1.put("imagen", "casa1.jpg");

        Map<String, Object> respuestaAnfitrion1 = new HashMap<>();
        respuestaAnfitrion1.put("respuesta", "¡Gracias Juan! Fue un placer tenerte como huésped");
        respuestaAnfitrion1.put("fecha", "2024-01-18T15:45:00");

        Map<String, Object> comentario1 = new HashMap<>();
        comentario1.put("id", 1);
        comentario1.put("alojamiento", alojamiento1);
        comentario1.put("calificacion", 5);
        comentario1.put("comentario", "Excelente lugar, muy recomendado para familias");
        comentario1.put("fecha", "2024-01-18T10:30:00");
        comentario1.put("respuestaAnfitrion", respuestaAnfitrion1);

        Map<String, Object> alojamiento2 = new HashMap<>();
        alojamiento2.put("id", 2);
        alojamiento2.put("titulo", "Apartamento Moderno Centro");
        alojamiento2.put("imagen", "apto1.jpg");

        Map<String, Object> comentario2 = new HashMap<>();
        comentario2.put("id", 2);
        comentario2.put("alojamiento", alojamiento2);
        comentario2.put("calificacion", 4);
        comentario2.put("comentario", "Muy cómodo y bien ubicado, recomendado");
        comentario2.put("fecha", "2023-12-23T14:20:00");
        comentario2.put("respuestaAnfitrion", null);

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalComentarios", 5);
        resumen.put("calificacionPromedio", 4.4);
        resumen.put("conRespuesta", 3);
        resumen.put("sinRespuesta", 2);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 5);
        paginacion.put("totalPaginas", 1);

        Map<String, Object> response = new HashMap<>();
        response.put("comentarios", List.of(comentario1, comentario2));
        response.put("resumen", resumen);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/alojamiento/{alojamientoId}")
    @Operation(summary = "Ver comentarios de un alojamiento específico",
            description = "Lista todos los comentarios públicos de un alojamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentarios obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alojamiento no encontrado")
    })
    public ResponseEntity<Map<String, Object>> verComentariosAlojamiento(
            @Parameter(description = "ID del alojamiento", required = true)
            @PathVariable Long alojamientoId,

            @Parameter(description = "Ordenar por: fecha, calificacion", example = "fecha")
            @RequestParam(defaultValue = "fecha") String ordenarPor,

            @Parameter(description = "Dirección: asc, desc", example = "desc")
            @RequestParam(defaultValue = "desc") String direccion,

            @Parameter(description = "Página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "5")
            @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> alojamiento = new HashMap<>();
        alojamiento.put("id", alojamientoId);
        alojamiento.put("titulo", "Casa Campestre La Calera");

        Map<String, Object> distribucion = new HashMap<>();
        distribucion.put("5estrellas", 6);
        distribucion.put("4estrellas", 4);
        distribucion.put("3estrellas", 2);
        distribucion.put("2estrellas", 0);
        distribucion.put("1estrella", 0);

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("calificacionPromedio", 4.3);
        estadisticas.put("totalComentarios", 12);
        estadisticas.put("distribucion", distribucion);

        Map<String, Object> usuario1 = new HashMap<>();
        usuario1.put("nombre", "María García");
        usuario1.put("iniciales", "MG");
        usuario1.put("fechaRegistro", "2023-05-10");

        Map<String, Object> respuestaAnfitrion1 = new HashMap<>();
        respuestaAnfitrion1.put("anfitrion", "Ana López");
        respuestaAnfitrion1.put("respuesta", "¡Muchas gracias María! Fue un placer tenerte como huésped. ¡Vuelve pronto!");
        respuestaAnfitrion1.put("fecha", "2024-01-16T09:15:00");

        Map<String, Object> util1 = new HashMap<>();
        util1.put("votosPositivos", 8);
        util1.put("votosNegativos", 1);

        Map<String, Object> comentario1 = new HashMap<>();
        comentario1.put("id", 1);
        comentario1.put("usuario", usuario1);
        comentario1.put("calificacion", 5);
        comentario1.put("comentario", "Excelente lugar para descansar en familia. La casa es muy cómoda y tiene una vista espectacular.");
        comentario1.put("fecha", "2024-01-15T16:30:00");
        comentario1.put("respuestaAnfitrion", respuestaAnfitrion1);
        comentario1.put("util", util1);

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("ordenarPor", ordenarPor);
        filtros.put("direccion", direccion);

        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 12);
        paginacion.put("totalPaginas", 3);

        Map<String, Object> response = new HashMap<>();
        response.put("alojamiento", alojamiento);
        response.put("estadisticas", estadisticas);
        response.put("comentarios", List.of(comentario1));
        response.put("filtros", filtros);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Ver reservas pendientes de comentar",
            description = "Lista de reservas completadas que aún no tienen comentario")
    public ResponseEntity<Map<String, Object>> reservasPendientesComentario() {
        Map<String, Object> alojamiento = new HashMap<>();
        alojamiento.put("id", 3);
        alojamiento.put("titulo", "Finca en Guatapé");
        alojamiento.put("imagen", "finca1.jpg");

        Map<String, Object> reservaPendiente = new HashMap<>();
        reservaPendiente.put("reservaId", 5);
        reservaPendiente.put("alojamiento", alojamiento);
        reservaPendiente.put("fechaCheckOut", "2024-01-20");
        reservaPendiente.put("diasTranscurridos", 5);
        reservaPendiente.put("puedeComentary", true);

        Map<String, Object> response = new HashMap<>();
        response.put("reservasPendientes", List.of(reservaPendiente));
        response.put("recordatorio", "¡Comparte tu experiencia para ayudar a otros viajeros!");
        response.put("beneficios", List.of(
                "Ayudas a otros viajeros a elegir",
                "Contribuyes a la comunidad",
                "Los anfitriones mejoran sus servicios"
        ));

        return ResponseEntity.ok(response);
    }
}