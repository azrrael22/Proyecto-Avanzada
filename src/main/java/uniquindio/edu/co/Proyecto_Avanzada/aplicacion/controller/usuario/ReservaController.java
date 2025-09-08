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
@RequestMapping("/api/usuario/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class ReservaController {

    @PostMapping
    @Operation(summary = "Realizar nueva reserva",
            description = "HU-U005: Crear reserva validando disponibilidad y capacidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fechas no disponibles"),
            @ApiResponse(responseCode = "409", description = "El alojamiento no está disponible en esas fechas"),
            @ApiResponse(responseCode = "422", description = "Excede la capacidad máxima del alojamiento")
    })
    public ResponseEntity<Map<String, Object>> realizarReserva(
            @Parameter(description = "ID del alojamiento a reservar", required = true, example = "1")
            @RequestParam Long alojamientoId,

            @Parameter(description = "Fecha de check-in en formato YYYY-MM-DD", required = true, example = "2024-02-15")
            @RequestParam String fechaCheckIn,

            @Parameter(description = "Fecha de check-out en formato YYYY-MM-DD", required = true, example = "2024-02-17")
            @RequestParam String fechaCheckOut,

            @Parameter(description = "Número de huéspedes (debe ser ≤ capacidad máxima)", required = true, example = "4")
            @RequestParam Integer numHuespedes
    ) {
        // Calcular noches y precio total (mock)
        int noches = 2; // Diferencia entre fechas
        double precioPorNoche = 150000;
        double precioTotal = noches * precioPorNoche;

        // Crear data del alojamiento
        Map<String, Object> alojamientoData = new HashMap<>();
        alojamientoData.put("id", alojamientoId);
        alojamientoData.put("titulo", "Casa Campestre La Calera");
        alojamientoData.put("imagen", "casa1.jpg");

        // Crear data de la reserva
        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("id", 1);
        reservaData.put("alojamiento", alojamientoData);
        reservaData.put("fechaCheckIn", fechaCheckIn);
        reservaData.put("fechaCheckOut", fechaCheckOut);
        reservaData.put("numHuespedes", numHuespedes);
        reservaData.put("noches", noches);
        reservaData.put("precioPorNoche", precioPorNoche);
        reservaData.put("precioTotal", precioTotal);
        reservaData.put("estado", "PENDIENTE");
        reservaData.put("fechaReserva", java.time.LocalDateTime.now().toString());

        // Crear respuesta completa
        Map<String, Object> response = new HashMap<>();
        response.put("reserva", reservaData);
        response.put("message", "¡Reserva creada exitosamente!");
        response.put("siguientesPasos", List.of(
                "Confirmación enviada al anfitrión",
                "Recibirás email de confirmación",
                "Puedes cancelar hasta 48 horas antes del check-in"
        ));

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @Operation(summary = "Ver historial de reservas",
            description = "HU-U007: Lista de reservas con filtros por estado y fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> historialReservas(
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
        // Crear alojamiento 1
        Map<String, Object> alojamiento1 = new HashMap<>();
        alojamiento1.put("id", 1);
        alojamiento1.put("titulo", "Casa Campestre La Calera");
        alojamiento1.put("imagen", "casa1.jpg");
        alojamiento1.put("ciudad", "La Calera");

        // Crear reserva 1
        Map<String, Object> reserva1 = new HashMap<>();
        reserva1.put("id", 1);
        reserva1.put("alojamiento", alojamiento1);
        reserva1.put("fechaCheckIn", "2024-02-15");
        reserva1.put("fechaCheckOut", "2024-02-17");
        reserva1.put("numHuespedes", 4);
        reserva1.put("precioTotal", 300000);
        reserva1.put("estado", "CONFIRMADA");
        reserva1.put("puedeComentary", false);
        reserva1.put("puedeCancelar", true);

        // Crear filtros
        Map<String, Object> filtros = new HashMap<>();
        filtros.put("estado", estado);
        filtros.put("fechaDesde", fechaDesde);
        filtros.put("fechaHasta", fechaHasta);

        // Crear resumen
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalReservas", 8);
        resumen.put("activas", 2);
        resumen.put("completadas", 5);
        resumen.put("canceladas", 1);

        // Crear paginación
        Map<String, Object> paginacion = new HashMap<>();
        paginacion.put("page", page);
        paginacion.put("size", size);
        paginacion.put("totalElementos", 8);
        paginacion.put("totalPaginas", 1);

        // Crear respuesta completa
        Map<String, Object> response = new HashMap<>();
        response.put("reservas", List.of(reserva1));
        response.put("filtros", filtros);
        response.put("resumen", resumen);
        response.put("paginacion", paginacion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalles de reserva específica")
    public ResponseEntity<Map<String, Object>> detalleReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id
    ) {
        // Crear data del anfitrión
        Map<String, Object> anfitrionData = new HashMap<>();
        anfitrionData.put("nombre", "María García");
        anfitrionData.put("telefono", "+57310987654");
        anfitrionData.put("email", "maria.garcia@email.com");

        // Crear data del alojamiento
        Map<String, Object> alojamientoData = new HashMap<>();
        alojamientoData.put("id", 1);
        alojamientoData.put("titulo", "Casa Campestre La Calera");
        alojamientoData.put("descripcion", "Hermosa casa con vista a los cerros");
        alojamientoData.put("imagen", "casa1.jpg");
        alojamientoData.put("direccion", "Vereda Alto del Águila, Km 2");
        alojamientoData.put("anfitrion", anfitrionData);

        // Crear data de la reserva
        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("id", id);
        reservaData.put("alojamiento", alojamientoData);
        reservaData.put("fechaCheckIn", "2024-02-15");
        reservaData.put("fechaCheckOut", "2024-02-17");
        reservaData.put("numHuespedes", 4);
        reservaData.put("noches", 2);
        reservaData.put("precioPorNoche", 150000);
        reservaData.put("precioTotal", 300000);
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("fechaReserva", "2024-01-10T14:30:00");
        reservaData.put("instrucciones", List.of(
                "Check-in: 3:00 PM",
                "Check-out: 11:00 AM",
                "Las llaves están en la caja de seguridad",
                "Código de acceso: 1234"
        ));

        // Crear respuesta completa
        Map<String, Object> response = new HashMap<>();
        response.put("reserva", reservaData);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar reserva",
            description = "HU-U006: Cancelación con validación de 48 horas y políticas de reembolso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede cancelar (menos de 48 horas)"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Reserva ya está cancelada o completada")
    })
    public ResponseEntity<Map<String, Object>> cancelarReserva(
            @Parameter(description = "ID de la reserva a cancelar", required = true)
            @PathVariable Long id,

            @Parameter(description = "Motivo de la cancelación")
            @RequestParam(required = false) String motivo
    ) {
        // Crear data de la reserva cancelada
        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("id", id);
        reservaData.put("estadoAnterior", "CONFIRMADA");
        reservaData.put("nuevoEstado", "CANCELADA");
        reservaData.put("fechaCancelacion", java.time.LocalDateTime.now().toString());
        reservaData.put("motivoCancelacion", motivo != null ? motivo : "No especificado");

        // Crear data del reembolso
        Map<String, Object> reembolsoData = new HashMap<>();
        reembolsoData.put("aplica", true);
        reembolsoData.put("porcentaje", 80);
        reembolsoData.put("monto", 240000);
        reembolsoData.put("tiempoEstimado", "5-7 días hábiles");
        reembolsoData.put("metodo", "Mismo método de pago utilizado");

        // Crear respuesta completa
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reserva cancelada exitosamente");
        response.put("reserva", reservaData);
        response.put("reembolso", reembolsoData);
        response.put("politicas", List.of(
                "Cancelación con más de 48 horas: 80% de reembolso",
                "El anfitrión ha sido notificado automáticamente",
                "Recibirás confirmación por email"
        ));

        return ResponseEntity.ok(response);
    }
}