package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.publico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.CityDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.FeaturedAccommodationDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.StatisticsDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.PublicService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para endpoints públicos (no requieren autenticación)
 * Estos endpoints son consumidos por la landing page
 */
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Público", description = "Endpoints públicos para visitantes no autenticados")
public class PublicController {

    private final PublicService publicService;

    /**
     * HU-V002: Obtener alojamientos destacados para la landing page
     * Retorna máximo 6 alojamientos con mejor calificación
     */
    @GetMapping("/accommodations/featured")
    @Operation(
            summary = "Obtener alojamientos destacados",
            description = "Retorna hasta 6 alojamientos destacados para mostrar en la página principal"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alojamientos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerAlojamientosDestacados() {
        try {
            List<FeaturedAccommodationDTO> alojamientos = publicService.obtenerAlojamientosDestacados();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", alojamientos);
            response.put("total", alojamientos.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al obtener alojamientos destacados");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Obtener estadísticas generales de la plataforma
     */
    @GetMapping("/statistics")
    @Operation(
            summary = "Obtener estadísticas de la plataforma",
            description = "Retorna estadísticas generales: alojamientos, usuarios, ciudades, reservas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            StatisticsDTO estadisticas = publicService.obtenerEstadisticas();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", estadisticas);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al obtener estadísticas");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Obtener lista de ciudades con alojamientos disponibles
     */
    @GetMapping("/cities")
    @Operation(
            summary = "Obtener ciudades disponibles",
            description = "Retorna lista de ciudades con cantidad de alojamientos disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudades obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerCiudades() {
        try {
            List<CityDTO> ciudades = publicService.obtenerCiudades();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", ciudades);
            response.put("total", ciudades.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al obtener ciudades");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
