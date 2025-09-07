package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(201).body(Map.of(
                "comentario", Map.of(
                        "id", 1,
                        "reserva", Map.of(
                                "id", reservaId,
                                "alojamiento", "Casa Campestre La Calera",
                                "fechaEstadia", "2024-01-15 a 2024-01-17"
                        ),
                        "calificacion", calificacion,
                        "comentario", comentario != null ? comentario : "",
                        "fecha", java.time.LocalDateTime.now().toString(),
                        "usuario", "Juan Pérez" // En producción viene del token JWT
                ),
                "message", "¡Gracias por tu comentario!",
                "impacto", Map.of(
                        "nuevoPromedio", 4.3,
                        "totalComentarios", 8,
                        "ayudasOtrosViajeros", true
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "comentarios", List.of(
                        Map.of(
                                "id", 1,
                                "alojamiento", Map.of(
                                        "id", 1,
                                        "titulo", "Casa Campestre La Calera",
                                        "imagen", "casa1.jpg"
                                ),
                                "calificacion", 5,
                                "comentario", "Excelente lugar, muy recomendado para familias",
                                "fecha", "2024-01-18T10:30:00",
                                "respuestaAnfitrion", Map.of(
                                        "respuesta", "¡Gracias Juan! Fue un placer tenerte como huésped",
                                        "fecha", "2024-01-18T15:45:00"
                                )
                        ),
                        Map.of(
                                "id", 2,
                                "alojamiento", Map.of(
                                        "id", 2,
                                        "titulo", "Apartamento Moderno Centro",
                                        "imagen", "apto1.jpg"
                                ),
                                "calificacion", 4,
                                "comentario", "Muy cómodo y bien ubicado, recomendado",
                                "fecha", "2023-12-23T14:20:00",
                                "respuestaAnfitrion", null
                        )
                ),
                "resumen", Map.of(
                        "totalComentarios", 5,
                        "calificacionPromedio", 4.4,
                        "conRespuesta", 3,
                        "sinRespuesta", 2
                ),
                "paginacion", Map.of(
                        "page", page,
                        "size", size,
                        "totalElementos", 5,
                        "totalPaginas", 1
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "alojamiento", Map.of(
                        "id", alojamientoId,
                        "titulo", "Casa Campestre La Calera"
                ),
                "estadisticas", Map.of(
                        "calificacionPromedio", 4.3,
                        "totalComentarios", 12,
                        "distribucion", Map.of(
                                "5estrellas", 6,
                                "4estrellas", 4,
                                "3estrellas", 2,
                                "2estrellas", 0,
                                "1estrella", 0
                        )
                ),
                "comentarios", List.of(
                        Map.of(
                                "id", 1,
                                "usuario", Map.of(
                                        "nombre", "María García",
                                        "iniciales", "MG",
                                        "fechaRegistro", "2023-05-10"
                                ),
                                "calificacion", 5,
                                "comentario", "Excelente lugar para descansar en familia. La casa es muy cómoda y tiene una vista espectacular.",
                                "fecha", "2024-01-15T16:30:00",
                                "respuestaAnfitrion", Map.of(
                                        "anfitrion", "Ana López",
                                        "respuesta", "¡Muchas gracias María! Fue un placer tenerte como huésped. ¡Vuelve pronto!",
                                        "fecha", "2024-01-16T09:15:00"
                                ),
                                "util", Map.of(
                                        "votosPositivos", 8,
                                        "votosNegativos", 1
                                )
                        ),
                        Map.of(
                                "id", 2,
                                "usuario", Map.of(
                                        "nombre", "Carlos Mendoza",
                                        "iniciales", "CM",
                                        "fechaRegistro", "2023-08-22"
                                ),
                                "calificacion", 4,
                                "comentario", "Muy buena ubicación y limpieza. Solo el WiFi podría mejorar un poco.",
                                "fecha", "2024-01-10T11:45:00",
                                "respuestaAnfitrion", Map.of(
                                        "anfitrion", "Ana López",
                                        "respuesta", "Gracias por el feedback Carlos, ya estamos mejorando la conexión WiFi.",
                                        "fecha", "2024-01-10T18:20:00"
                                ),
                                "util", Map.of(
                                        "votosPositivos", 5,
                                        "votosNegativos", 0
                                )
                        )
                ),
                "filtros", Map.of(
                        "ordenarPor", ordenarPor,
                        "direccion", direccion
                ),
                "paginacion", Map.of(
                        "page", page,
                        "size", size,
                        "totalElementos", 12,
                        "totalPaginas", 3
                )
        ));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Ver reservas pendientes de comentar",
            description = "Lista de reservas completadas que aún no tienen comentario")
    public ResponseEntity<Map<String, Object>> reservasPendientesComentario() {
        return ResponseEntity.ok(Map.of(
                "reservasPendientes", List.of(
                        Map.of(
                                "reservaId", 5,
                                "alojamiento", Map.of(
                                        "id", 3,
                                        "titulo", "Finca en Guatapé",
                                        "imagen", "finca1.jpg"
                                ),
                                "fechaCheckOut", "2024-01-20",
                                "diasTranscurridos", 5,
                                "puedeComentary", true
                        )
                ),
                "recordatorio", "¡Comparte tu experiencia para ayudar a otros viajeros!",
                "beneficios", List.of(
                        "Ayudas a otros viajeros a elegir",
                        "Contribuyes a la comunidad",
                        "Los anfitriones mejoran sus servicios"
                )
        ));
    }
}