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
@RequestMapping("/api/usuario")
@Tag(name = "Usuario", description = "Operaciones para usuarios autenticados")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    @GetMapping("/alojamientos")
    @Operation(summary = "Buscar alojamientos",
            description = "HU-U003: Búsqueda de alojamientos con filtros (ciudad, fechas, precio, tipo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> buscarAlojamientos(
            @Parameter(description = "Ciudad de búsqueda", example = "Bogotá")
            @RequestParam(required = false) String ciudad,

            @Parameter(description = "Fecha de check-in en formato YYYY-MM-DD", example = "2024-02-15")
            @RequestParam(required = false) String fechaCheckIn,

            @Parameter(description = "Fecha de check-out en formato YYYY-MM-DD", example = "2024-02-17")
            @RequestParam(required = false) String fechaCheckOut,

            @Parameter(description = "Precio mínimo por noche", example = "50000")
            @RequestParam(required = false) Double precioMin,

            @Parameter(description = "Precio máximo por noche", example = "200000")
            @RequestParam(required = false) Double precioMax,

            @Parameter(description = "Tipo de alojamiento: CASA, APARTAMENTO, FINCA", example = "CASA")
            @RequestParam(required = false) String tipo,

            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(Map.of(
                "filtros", Map.of(
                        "ciudad", ciudad,
                        "fechaCheckIn", fechaCheckIn,
                        "fechaCheckOut", fechaCheckOut,
                        "rangoPrecios", (precioMin != null && precioMax != null) ? precioMin + " - " + precioMax : "Sin filtro",
                        "tipo", tipo
                ),
                "resultados", List.of(
                        Map.of("id", 1, "titulo", "Casa Campestre La Calera", "ciudad", "La Calera", "precio", 150000,
                                "calificacion", 4.5, "imagen", "casa1.jpg", "capacidad", 6, "tipo", "CASA"),
                        Map.of("id", 2, "titulo", "Apartamento Moderno Centro", "ciudad", "Bogotá", "precio", 120000,
                                "calificacion", 4.2, "imagen", "apto1.jpg", "capacidad", 4, "tipo", "APARTAMENTO"),
                        Map.of("id", 3, "titulo", "Finca en Guatapé", "ciudad", "Guatapé", "precio", 200000,
                                "calificacion", 4.8, "imagen", "finca1.jpg", "capacidad", 8, "tipo", "FINCA")
                ),
                "paginacion", Map.of(
                        "page", page,
                        "size", size,
                        "totalElementos", 25,
                        "totalPaginas", 3
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "alojamiento", Map.of(
                        "id", id,
                        "titulo", "Casa Campestre La Calera",
                        "descripcion", "Hermosa casa con vista panorámica a los cerros orientales. Ideal para descansar en familia o con amigos. Cuenta con amplios espacios, jardín y zona de asador.",
                        "anfitrion", Map.of(
                                "nombre", "María García",
                                "foto", "maria.jpg",
                                "fechaRegistro", "2023-01-15"
                        ),
                        "capacidad", 6,
                        "precio", 150000,
                        "tipo", "CASA",
                        "ciudad", "La Calera",
                        "direccion", "Vereda Alto del Águila, Km 2"
                ),
                "galeria", List.of(
                        Map.of("url", "casa1_1.jpg", "esPrincipal", true, "orden", 1),
                        Map.of("url", "casa1_2.jpg", "esPrincipal", false, "orden", 2),
                        Map.of("url", "casa1_3.jpg", "esPrincipal", false, "orden", 3),
                        Map.of("url", "casa1_4.jpg", "esPrincipal", false, "orden", 4)
                ),
                "servicios", List.of("WiFi", "Piscina", "Parqueadero", "Asador", "Chimenea", "Jardín", "Cocina completa"),
                "ubicacion", Map.of(
                        "latitud", 4.7315,
                        "longitud", -74.0431,
                        "descripcion", "A 30 minutos de Bogotá"
                ),
                "disponibilidad", List.of(
                        "2024-02-15", "2024-02-16", "2024-02-17", "2024-02-20", "2024-02-21"
                ),
                "comentarios", Map.of(
                        "promedio", 4.5,
                        "total", 12,
                        "lista", List.of(
                                Map.of("usuario", "Juan Pérez", "calificacion", 5, "comentario", "Excelente lugar, muy recomendado",
                                        "fecha", "2024-01-15", "respuesta", "Gracias por tu comentario, Juan!"),
                                Map.of("usuario", "Ana Rodríguez", "calificacion", 4, "comentario", "Muy cómodo y limpio",
                                        "fecha", "2024-01-10", "respuesta", null)
                        )
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "message", "Felicidades! Ahora eres un anfitrión",
                "usuario", Map.of(
                        "id", 1,
                        "nombre", "Juan Pérez",
                        "rolAnterior", "USUARIO",
                        "nuevoRol", "ANFITRION",
                        "fechaCambio", java.time.LocalDateTime.now().toString()
                ),
                "beneficios", List.of(
                        "Puedes crear y gestionar alojamientos",
                        "Recibir reservas de huéspedes",
                        "Generar ingresos con tus propiedades",
                        "Acceso a métricas y estadísticas"
                )
        ));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard de usuario",
            description = "Resumen de actividad del usuario: reservas recientes, favoritos, etc.")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(Map.of(
                "resumen", Map.of(
                        "reservasActivas", 2,
                        "reservasCompletadas", 5,
                        "comentariosPendientes", 1,
                        "favoritos", 3
                ),
                "reservasRecientes", List.of(
                        Map.of("id", 1, "alojamiento", "Casa Campestre", "estado", "CONFIRMADA", "fechas", "2024-02-15 a 2024-02-17"),
                        Map.of("id", 2, "alojamiento", "Apartamento Centro", "estado", "PENDIENTE", "fechas", "2024-03-01 a 2024-03-03")
                ),
                "recomendaciones", List.of(
                        Map.of("id", 10, "titulo", "Villa en Melgar", "precio", 180000, "calificacion", 4.6),
                        Map.of("id", 11, "titulo", "Casa en Girardot", "precio", 140000, "calificacion", 4.3)
                )
        ));
    }
}
