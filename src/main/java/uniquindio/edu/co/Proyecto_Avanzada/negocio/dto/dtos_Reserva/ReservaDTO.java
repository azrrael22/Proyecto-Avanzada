package uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;


/**
 * DTO COMPLETO de reservas - Para LECTURA/RESPUESTAS
 *
 * Incluye información denormalizada del alojamiento y usuario para
 * evitar consultas adicionales en el frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de la reserva")
public class ReservaDTO {
    @Schema(description = "ID único de la reserva", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    // Información del alojamiento (denormalizada)
    @Schema(description = "ID del alojamiento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long alojamientoId;

    @Schema(description = "Título del alojamiento", example = "Casa Campestre La Calera", accessMode = Schema.AccessMode.READ_ONLY)
    private String alojamientoTitulo;

    @Schema(description = "Imagen del alojamiento", example = "casa1.jpg", accessMode = Schema.AccessMode.READ_ONLY)
    private String alojamientoImagen;

    @Schema(description = "Ciudad del alojamiento", example = "La Calera", accessMode = Schema.AccessMode.READ_ONLY)
    private String alojamientoCiudad;

    // Datos de la reserva
    @Schema(description = "Fecha de check-in", example = "2024-02-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaCheckIn;

    @Schema(description = "Fecha de check-out", example = "2024-02-17", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaCheckOut;

    @Schema(description = "Número de huéspedes", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numHuespedes;

    @Schema(description = "Número de noches", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer noches;

    @Schema(description = "Precio por noche", example = "150000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precioPorNoche;

    @Schema(description = "Precio total", example = "300000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precioTotal;

    @Schema(description = "Estado de la reserva", example = "CONFIRMADA", accessMode = Schema.AccessMode.READ_ONLY)
    private EstadoReserva estado;

    @Schema(description = "Fecha de creación", example = "2024-01-10T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaReserva;

    @Schema(description = "Fecha de cancelación", example = "2024-01-12T09:15:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCancelacion;

    @Schema(description = "Motivo de cancelación", example = "Cambio de planes", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoCancelacion;

    // Para anfitriones - información del huésped (denormalizada)
    @Schema(description = "Nombre del huésped", example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String huespedNombre;

    @Schema(description = "Email del huésped", example = "juan.perez@email.com", accessMode = Schema.AccessMode.READ_ONLY)
    private String huespedEmail;

    @Schema(description = "Teléfono del huésped", example = "+57300123456", accessMode = Schema.AccessMode.READ_ONLY)
    private String huespedTelefono;

    // Acciones disponibles (calculadas)
    @Schema(description = "Permite cancelación", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean puedeCancelar;

    @Schema(description = "Permite comentar", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean puedeComentary;
}
