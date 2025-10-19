// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/AlojamientoService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoUpdateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.BusquedaAlojamientosDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import java.util.List;

public interface AlojamientoService {

    /**
     * Crea un nuevo alojamiento para un anfitrión específico.
     * @param alojamientoCreateDTO Los datos para la creación del alojamiento. [cite: 3004]
     * @param anfitrionId El ID del anfitrión que está creando el alojamiento.
     * @return El DTO del alojamiento recién creado con todos sus detalles. [cite: 3017]
     * @throws Exception Si el anfitrión no existe o hay un error de validación.
     */
    AlojamientoDTO crearAlojamiento(AlojamientoCreateDTO alojamientoCreateDTO, Long anfitrionId) throws Exception;

    /**
     * Lista todos los alojamientos de un anfitrión específico.
     * @param anfitrionId El ID del anfitrión.
     * @return Una lista con la información resumida de cada alojamiento.
     */
    List<AlojamientoSummaryDTO> listarAlojamientosPorAnfitrion(Long anfitrionId);

    /**
     * Actualiza un alojamiento existente.
     * @param alojamientoId El ID del alojamiento a actualizar.
     * @param alojamientoUpdateDTO El DTO con los datos a modificar.
     * @param anfitrionId El ID del anfitrión que realiza la petición, para verificar permisos.
     * @return El DTO del alojamiento con los datos ya actualizados.
     * @throws Exception Si el alojamiento no se encuentra o el anfitrión no es el propietario.
     */
    AlojamientoDTO actualizarAlojamiento(Long alojamientoId, AlojamientoUpdateDTO alojamientoUpdateDTO, Long anfitrionId) throws Exception;

    /**
     * Realiza un borrado lógico (soft delete) de un alojamiento.
     * @param alojamientoId El ID del alojamiento a eliminar.
     * @param anfitrionId El ID del anfitrión que realiza la acción.
     * @throws Exception Si el alojamiento no existe, no pertenece al anfitrión o tiene reservas futuras.
     */
    void eliminarAlojamiento(Long alojamientoId, Long anfitrionId) throws Exception;

    /**
     * Cambia el estado de un alojamiento (ACTIVO/INACTIVO).
     * @param alojamientoId El ID del alojamiento a modificar.
     * @param nuevoEstado El nuevo estado deseado (ACTIVO o INACTIVO).
     * @param anfitrionId El ID del anfitrión que realiza la acción, para verificar permisos.
     * @return El DTO del alojamiento con el estado actualizado.
     * @throws Exception Si el alojamiento no se encuentra, no pertenece al anfitrión,
     * o si se intenta inactivar con reservas futuras activas.
     */
    AlojamientoDTO cambiarEstadoAlojamiento(Long alojamientoId, EstadoAlojamiento nuevoEstado, Long anfitrionId) throws Exception;

    List<AlojamientoSummaryDTO> buscarAlojamientosDisponibles(BusquedaAlojamientosDTO filtros);

}