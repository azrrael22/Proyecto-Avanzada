// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/ReservaService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;

public interface ReservaService {

    /**
     * Crea una nueva reserva, aplicando todas las validaciones de negocio.
     * @param reservaCreateDTO Datos para crear la reserva.
     * @param usuarioId ID del usuario que realiza la reserva.
     * @return DTO de la reserva creada.
     * @throws Exception Si alguna validación falla (disponibilidad, capacidad, etc.).
     */
    ReservaDTO crearReserva(ReservaCreateDTO reservaCreateDTO, Long usuarioId) throws Exception;

}