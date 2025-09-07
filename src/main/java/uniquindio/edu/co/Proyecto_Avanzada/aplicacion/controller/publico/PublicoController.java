package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.publico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publico")
@Tag(name = "Público", description = "Endpoints públicos para visitantes no autenticados")
public class PublicoController {

    @GetMapping("/inicio")
    @Operation(summary = "Información de la página de inicio",
            description = "HU-V001: Información general, testimonios y estadísticas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, Object>> paginaInicio() {
        return ResponseEntity.ok(Map.of(
                "titulo", "Sistema de Gestión de Alojamientos",
                "descripcion", "Encuentra el alojamiento perfecto para tu próxima aventura",
                "estadisticas", Map.of(
                        "totalAlojamientos", 150,
                        "totalUsuarios", 500,
                        "ciudadesDisponibles", 25
                ),
                "testimonios", List.of(
                        Map.of("usuario", "María García", "comentario", "Excelente servicio, muy recomendado"),
                        Map.of("usuario", "Carlos López", "comentario", "Muy fácil de usar y encontré el lugar perfecto")
                )
        ));
    }

    @GetMapping("/alojamientos/preview")
    @Operation(summary = "Vista previa limitada de alojamientos",
            description = "HU-V002: Máximo 6 alojamientos destacados para visitantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alojamientos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Map<String, Object>>> vistaPrevia() {
        List<Map<String, Object>> alojamientos = List.of(
                Map.of("id", 1, "titulo", "Casa Campestre La Calera", "ciudad", "La Calera", "precio", 150000, "imagen", "casa1.jpg", "calificacion", 4.5),
                Map.of("id", 2, "titulo", "Apartamento Moderno Bogotá", "ciudad", "Bogotá", "precio", 120000, "imagen", "apto1.jpg", "calificacion", 4.2),
                Map.of("id", 3, "titulo", "Finca en Guatapé", "ciudad", "Guatapé", "precio", 200000, "imagen", "finca1.jpg", "calificacion", 4.8),
                Map.of("id", 4, "titulo", "Casa Colonial Villa de Leyva", "ciudad", "Villa de Leyva", "precio", 180000, "imagen", "casa2.jpg", "calificacion", 4.6),
                Map.of("id", 5, "titulo", "Apartamento en Cartagena", "ciudad", "Cartagena", "precio", 250000, "imagen", "apto2.jpg", "calificacion", 4.7),
                Map.of("id", 6, "titulo", "Cabaña en el Eje Cafetero", "ciudad", "Manizales", "precio", 160000, "imagen", "cabana1.jpg", "calificacion", 4.4)
        );
        return ResponseEntity.ok(alojamientos);
    }

    @GetMapping("/health")
    @Operation(summary = "Verificar estado del servicio")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Sistema funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}