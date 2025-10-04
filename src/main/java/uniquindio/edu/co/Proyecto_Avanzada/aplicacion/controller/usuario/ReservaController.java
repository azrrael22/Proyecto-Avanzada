
package uniquindio.edu.co.Proyecto_Avanzada.aplicacion.controller.usuario;

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
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.service.ReservaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @Operation(summary = "Realizar nueva reserva", description = "HU-U005: Crear reserva validando disponibilidad y capacidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fechas no disponibles"),
            @ApiResponse(responseCode = "409", description = "El alojamiento no está disponible en esas fechas"),
            @ApiResponse(responseCode = "422", description = "Excede la capacidad máxima del alojamiento")
    })
    public ResponseEntity<Map<String, Object>> realizarReserva(@RequestBody ReservaCreateDTO reservaCreateDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // NOTA: El ID del usuario que reserva se debe obtener del token de seguridad.
            // Para probar, usaremos un ID fijo.
            Long usuarioId = 1L;

            var reservaCreada = reservaService.crearReserva(reservaCreateDTO, usuarioId);

            response.put("message", "¡Reserva creada exitosamente!");
            response.put("reserva", reservaCreada);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            // 409 Conflict es un buen código para "no disponible"
            if (e.getMessage().contains("no está disponible")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            // 400 Bad Request para otras validaciones fallidas
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Ver historial de reservas", description = "HU-U007: Lista de reservas con filtros por estado y fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<Map<String, Object>> historialReservas(
            @Parameter(description = "Filtro por estado: PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA") @RequestParam(required = false) String estado,
            @Parameter(description = "Fecha desde (YYYY-MM-DD)") @RequestParam(required = false) String fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)") @RequestParam(required = false) String fechaHasta,
            @Parameter(description = "Página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ID de usuario fijo para probar. Más adelante se obtendrá del token.
            Long usuarioId = 1L;

            List<ReservaDTO> historial = reservaService.listarReservasPorUsuario(usuarioId);

            // Mantenemos la estructura de respuesta original dentro de un mapa.
            response.put("reservas", historial);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        reservaData.put("fechaCheckin", "2024-02-15");
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
    @Operation(summary = "Cancelar reserva", description = "HU-U006: Cancelación con validación de 48 horas y políticas de reembolso")
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