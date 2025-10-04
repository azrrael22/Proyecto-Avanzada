// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/AlojamientoService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoDTO;

public interface AlojamientoService {

    /**
     * Crea un nuevo alojamiento para un anfitrión específico.
     * @param alojamientoCreateDTO Los datos para la creación del alojamiento. [cite: 3004]
     * @param anfitrionId El ID del anfitrión que está creando el alojamiento.
     * @return El DTO del alojamiento recién creado con todos sus detalles. [cite: 3017]
     * @throws Exception Si el anfitrión no existe o hay un error de validación.
     */
    AlojamientoDTO crearAlojamiento(AlojamientoCreateDTO alojamientoCreateDTO, Long anfitrionId) throws Exception;

}