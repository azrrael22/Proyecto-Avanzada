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

        return ResponseEntity.status(201).body(Map.of(
                "reserva", Map.of(
                        "id", 1,
                        "alojamiento", Map.of(
                                "id", alojamientoId,
                                "titulo", "Casa Campestre La Calera",
                                "imagen", "casa1.jpg"
                        ),
                        "fechaCheckIn", fechaCheckIn,
                        "fechaCheckOut", fechaCheckOut,
                        "numHuespedes", numHuespedes,
                        "noches", noches,
                        "precioPorNoche", precioPorNoche,
                        "precioTotal", precioTotal,
                        "estado", "PENDIENTE",
                        "fechaReserva", java.time.LocalDateTime.now().toString()
                ),
                "message", "¡Reserva creada exitosamente!",
                "siguientesPasos", List.of(
                        "Confirmación enviada al anfitrión",
                        "Recibirás email de confirmación",
                        "Puedes cancelar hasta 48 horas antes del check-in"
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "reservas", List.of(
                        Map.of(
                                "id", 1,
                                "alojamiento", Map.of(
                                        "id", 1,
                                        "titulo", "Casa Campestre La Calera",
                                        "imagen", "casa1.jpg",
                                        "ciudad", "La Calera"
                                ),
                                "fechaCheckIn", "2024-02-15",
                                "fechaCheckOut", "2024-02-17",
                                "numHuespedes", 4,
                                "precioTotal", 300000,
                                "estado", "CONFIRMADA",
                                "puedeComentary", false,
                                "puedeCancelar", true
                        ),
                        Map.of(
                                "id", 2,
                                "alojamiento", Map.of(
                                        "id", 2,
                                        "titulo", "Apartamento Moderno Centro",
                                        "imagen", "apto1.jpg",
                                        "ciudad", "Bogotá"
                                ),
                                "fechaCheckIn", "2023-12-20",
                                "fechaCheckOut", "2023-12-22",
                                "numHuespedes", 2,
                                "precioTotal", 240000,
                                "estado", "COMPLETADA",
                                "puedeComentary", true,
                                "puedeCancelar", false
                        ),
                        Map.of(
                                "id", 3,
                                "alojamiento", Map.of(
                                        "id", 3,
                                        "titulo", "Finca en Guatapé",
                                        "imagen", "finca1.jpg",
                                        "ciudad", "Guatapé"
                                ),
                                "fechaCheckIn", "2024-03-10",
                                "fechaCheckOut", "2024-03-12",
                                "numHuespedes", 6,
                                "precioTotal", 400000,
                                "estado", "CANCELADA",
                                "motivoCancelacion", "Cambio de planes",
                                "puedeComentary", false,
                                "puedeCancelar", false
                        )
                ),
                "filtros", Map.of(
                        "estado", estado,
                        "fechaDesde", fechaDesde,
                        "fechaHasta", fechaHasta
                ),
                "resumen", Map.of(
                        "totalReservas", 8,
                        "activas", 2,
                        "completadas", 5,
                        "canceladas", 1
                ),
                "paginacion", Map.of(
                        "page", page,
                        "size", size,
                        "totalElementos", 8,
                        "totalPaginas", 1
                )
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalles de reserva específica")
    public ResponseEntity<Map<String, Object>> detalleReserva(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(Map.of(
                "reserva", Map.of(
                        "id", id,
                        "alojamiento", Map.of(
                                "id", 1,
                                "titulo", "Casa Campestre La Calera",
                                "descripcion", "Hermosa casa con vista a los cerros",
                                "imagen", "casa1.jpg",
                                "direccion", "Vereda Alto del Águila, Km 2",
                                "anfitrion", Map.of(
                                        "nombre", "María García",
                                        "telefono", "+57310987654",
                                        "email", "maria.garcia@email.com"
                                )
                        ),
                        "fechaCheckIn", "2024-02-15",
                        "fechaCheckOut", "2024-02-17",
                        "numHuespedes", 4,
                        "noches", 2,
                        "precioPorNoche", 150000,
                        "precioTotal", 300000,
                        "estado", "CONFIRMADA",
                        "fechaReserva", "2024-01-10T14:30:00",
                        "instrucciones", List.of(
                                "Check-in: 3:00 PM",
                                "Check-out: 11:00 AM",
                                "Las llaves están en la caja de seguridad",
                                "Código de acceso: 1234"
                        )
                )
        ));
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
        return ResponseEntity.ok(Map.of(
                "message", "Reserva cancelada exitosamente",
                "reserva", Map.of(
                        "id", id,
                        "estadoAnterior", "CONFIRMADA",
                        "nuevoEstado", "CANCELADA",
                        "fechaCancelacion", java.time.LocalDateTime.now().toString(),
                        "motivoCancelacion", motivo != null ? motivo : "No especificado"
                ),
                "reembolso", Map.of(
                        "aplica", true,
                        "porcentaje", 80,
                        "monto", 240000,
                        "tiempoEstimado", "5-7 días hábiles",
                        "metodo", "Mismo método de pago utilizado"
                ),
                "politicas", List.of(
                        "Cancelación con más de 48 horas: 80% de reembolso",
                        "El anfitrión ha sido notificado automáticamente",
                        "Recibirás confirmación por email"
                )
        ));
    }
}