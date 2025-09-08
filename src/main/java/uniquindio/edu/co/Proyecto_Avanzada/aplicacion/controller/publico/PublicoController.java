package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.publico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalAlojamientos", 150);
        estadisticas.put("totalUsuarios", 500);
        estadisticas.put("ciudadesDisponibles", 25);

        Map<String, Object> testimonio1 = new HashMap<>();
        testimonio1.put("usuario", "María García");
        testimonio1.put("comentario", "Excelente servicio, muy recomendado");

        Map<String, Object> testimonio2 = new HashMap<>();
        testimonio2.put("usuario", "Carlos López");
        testimonio2.put("comentario", "Muy fácil de usar y encontré el lugar perfecto");

        Map<String, Object> response = new HashMap<>();
        response.put("titulo", "Sistema de Gestión de Alojamientos");
        response.put("descripcion", "Encuentra el alojamiento perfecto para tu próxima aventura");
        response.put("estadisticas", estadisticas);
        response.put("testimonios", List.of(testimonio1, testimonio2));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/alojamientos/preview")
    @Operation(summary = "Vista previa limitada de alojamientos",
            description = "HU-V002: Máximo 6 alojamientos destacados para visitantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alojamientos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Map<String, Object>>> vistaPrevia() {
        Map<String, Object> alojamiento1 = new HashMap<>();
        alojamiento1.put("id", 1);
        alojamiento1.put("titulo", "Casa Campestre La Calera");
        alojamiento1.put("ciudad", "La Calera");
        alojamiento1.put("precio", 150000);
        alojamiento1.put("imagen", "casa1.jpg");
        alojamiento1.put("calificacion", 4.5);

        Map<String, Object> alojamiento2 = new HashMap<>();
        alojamiento2.put("id", 2);
        alojamiento2.put("titulo", "Apartamento Moderno Bogotá");
        alojamiento2.put("ciudad", "Bogotá");
        alojamiento2.put("precio", 120000);
        alojamiento2.put("imagen", "apto1.jpg");
        alojamiento2.put("calificacion", 4.2);

        Map<String, Object> alojamiento3 = new HashMap<>();
        alojamiento3.put("id", 3);
        alojamiento3.put("titulo", "Finca en Guatapé");
        alojamiento3.put("ciudad", "Guatapé");
        alojamiento3.put("precio", 200000);
        alojamiento3.put("imagen", "finca1.jpg");
        alojamiento3.put("calificacion", 4.8);

        Map<String, Object> alojamiento4 = new HashMap<>();
        alojamiento4.put("id", 4);
        alojamiento4.put("titulo", "Casa Colonial Villa de Leyva");
        alojamiento4.put("ciudad", "Villa de Leyva");
        alojamiento4.put("precio", 180000);
        alojamiento4.put("imagen", "casa2.jpg");
        alojamiento4.put("calificacion", 4.6);

        Map<String, Object> alojamiento5 = new HashMap<>();
        alojamiento5.put("id", 5);
        alojamiento5.put("titulo", "Apartamento en Cartagena");
        alojamiento5.put("ciudad", "Cartagena");
        alojamiento5.put("precio", 250000);
        alojamiento5.put("imagen", "apto2.jpg");
        alojamiento5.put("calificacion", 4.7);

        Map<String, Object> alojamiento6 = new HashMap<>();
        alojamiento6.put("id", 6);
        alojamiento6.put("titulo", "Cabaña en el Eje Cafetero");
        alojamiento6.put("ciudad", "Manizales");
        alojamiento6.put("precio", 160000);
        alojamiento6.put("imagen", "cabana1.jpg");
        alojamiento6.put("calificacion", 4.4);

        List<Map<String, Object>> alojamientos = List.of(
                alojamiento1, alojamiento2, alojamiento3,
                alojamiento4, alojamiento5, alojamiento6
        );

        return ResponseEntity.ok(alojamientos);
    }

    @GetMapping("/health")
    @Operation(summary = "Verificar estado del servicio")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Sistema funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}