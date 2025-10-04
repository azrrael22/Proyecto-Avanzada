// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/ReservaServiceImpl.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.ReservaMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.AlojamientoRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ReservaRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AlojamientoRepository alojamientoRepository;
    @Autowired private ReservaMapper reservaMapper;

    @Override
    public ReservaDTO crearReserva(ReservaCreateDTO reservaCreateDTO, Long usuarioId) throws Exception {
        // 1. Buscamos las entidades principales: el usuario que reserva y el alojamiento a reservar.
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new Exception("El usuario con ID " + usuarioId + " no existe."));

        AlojamientoEntity alojamiento = alojamientoRepository.findById(reservaCreateDTO.getAlojamientoId())
                .orElseThrow(() -> new Exception("El alojamiento con ID " + reservaCreateDTO.getAlojamientoId() + " no existe."));

        // 2. Aplicamos todas las reglas de negocio de los requisitos.
        validarReglasDeNegocio(reservaCreateDTO, alojamiento);

        // 3. Calculamos el precio total.
        long noches = ChronoUnit.DAYS.between(reservaCreateDTO.getFechaCheckIn(), reservaCreateDTO.getFechaCheckOut());
        BigDecimal precioTotal = alojamiento.getPrecioPorNoche().multiply(new BigDecimal(noches));

        // 4. Creamos la entidad de la reserva y asignamos los datos.
        ReservaEntity nuevaReserva = reservaMapper.toEntity(reservaCreateDTO);
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setAlojamiento(alojamiento);
        nuevaReserva.setPrecioTotal(precioTotal);

        // 5. Guardamos la nueva reserva en la BD.
        ReservaEntity reservaGuardada = reservaRepository.save(nuevaReserva);

        // 6. Retornamos el DTO de la reserva creada.
        return reservaMapper.toDTO(reservaGuardada);
    }

    private void validarReglasDeNegocio(ReservaCreateDTO reserva, AlojamientoEntity alojamiento) throws Exception {
        // Regla: No se pueden reservar fechas pasadas
        if (reserva.getFechaCheckIn().isBefore(LocalDate.now())) {
            throw new Exception("No se pueden reservar fechas en el pasado.");
        }

        // Regla: Mínimo 1 noche (check-out debe ser después de check-in)
        if (!reserva.getFechaCheckOut().isAfter(reserva.getFechaCheckIn())) {
            throw new Exception("La fecha de check-out debe ser posterior a la de check-in.");
        }

        // Regla: No superar la capacidad máxima del alojamiento
        if (reserva.getNumHuespedes() > alojamiento.getCapacidadMaxima()) {
            throw new Exception("El número de huéspedes (" + reserva.getNumHuespedes() + ") supera la capacidad máxima del alojamiento (" + alojamiento.getCapacidadMaxima() + ").");
        }

        // Regla de ORO: Validar que no haya solapamiento con otras reservas
        boolean estaDisponible = reservaRepository.isAvailable(
                alojamiento.getId(),
                reserva.getFechaCheckIn(),
                reserva.getFechaCheckOut()
        );

        if (!estaDisponible) {
            throw new Exception("El alojamiento no está disponible en las fechas seleccionadas.");
        }
    }

    @Override
    public List<ReservaDTO> listarReservasPorUsuario(Long usuarioId) {
        // 1. Buscamos todas las entidades de reserva para el ID de usuario.
        List<ReservaEntity> reservas = reservaRepository.findByUsuario_Id(usuarioId, Pageable.unpaged()).getContent();

        // 2. Las convertimos a una lista de DTOs y las retornamos.
        return reservaMapper.toDTOList(reservas);
    }

    @Override
    public void cancelarReserva(Long reservaId, Long usuarioId) throws Exception {
        // 1. Buscamos la reserva en la base de datos.
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new Exception("La reserva con ID " + reservaId + " no existe."));

        // 2. Validamos que el usuario que cancela sea el dueño de la reserva.
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            throw new Exception("No tienes permiso para cancelar esta reserva.");
        }

        // 3. Validamos que la reserva esté en un estado que permita su cancelación.
        if (reserva.getEstado() != EstadoReserva.PENDIENTE && reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new Exception("Esta reserva no se puede cancelar porque ya está " + reserva.getEstado().toString().toLowerCase() + ".");
        }

        // 4. Regla de Negocio: No se puede cancelar con menos de 48 horas de anticipación.
        if (reserva.getFechaCheckIn().isBefore(LocalDate.now().plusDays(2))) {
            throw new Exception("No se puede cancelar una reserva con menos de 48 horas de anticipación.");
        }

        // 5. Si todas las validaciones pasan, actualizamos el estado y la fecha de cancelación.
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setFechaCancelacion(LocalDateTime.now());

        // 6. Guardamos los cambios en la base de datos.
        reservaRepository.save(reserva);
    }
}