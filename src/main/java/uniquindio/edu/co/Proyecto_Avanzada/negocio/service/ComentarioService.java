// Ruta: negocio/service/ComentarioService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioDTO;

public interface ComentarioService {

    /**
     * Permite a un usuario dejar un comentario y calificación en una reserva completada.
     * @param comentarioCreateDTO Datos para crear el comentario.
     * @param usuarioId ID del usuario que deja el comentario.
     * @return El DTO del comentario recién creado.
     * @throws Exception Si la reserva no existe, no pertenece al usuario, no está completada o ya fue comentada.
     */
    ComentarioDTO dejarComentario(ComentarioCreateDTO comentarioCreateDTO, Long usuarioId) throws Exception;

}
