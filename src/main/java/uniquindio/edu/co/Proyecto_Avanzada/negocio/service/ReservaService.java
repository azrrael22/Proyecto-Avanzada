// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/ReservaService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;

import java.util.List;

public interface ReservaService {

    /**
     * Crea una nueva reserva, aplicando todas las validaciones de negocio.
     * @param reservaCreateDTO Datos para crear la reserva.
     * @param usuarioId ID del usuario que realiza la reserva.
     * @return DTO de la reserva creada.
     * @throws Exception Si alguna validación falla (disponibilidad, capacidad, etc.).
     */
    ReservaDTO crearReserva(ReservaCreateDTO reservaCreateDTO, Long usuarioId) throws Exception;

    /**
     * Obtiene el historial de reservas de un usuario.
     * @param usuarioId ID del usuario.
     * @return Lista de DTOs de las reservas del usuario.
     */
    List<ReservaDTO> listarReservasPorUsuario(Long usuarioId);

    /**
     * Cancela una reserva, validando permisos y la regla de las 48 horas.
     * @param reservaId ID de la reserva a cancelar.
     * @param usuarioId ID del usuario que solicita la cancelación.
     * @throws Exception Si la reserva no existe, no pertenece al usuario o no se puede cancelar.
     */
    void cancelarReserva(Long reservaId, Long usuarioId) throws Exception;

}