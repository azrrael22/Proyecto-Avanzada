// Ruta: negocio/service/ComentarioServiceImpl.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ComentarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.ComentarioMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ComentarioRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ReservaRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired private ComentarioRepository comentarioRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private ComentarioMapper comentarioMapper;

    @Override
    public ComentarioDTO dejarComentario(ComentarioCreateDTO comentarioCreateDTO, Long usuarioId) throws Exception {
        // 1. Buscamos la reserva que se quiere comentar.
        ReservaEntity reserva = reservaRepository.findById(comentarioCreateDTO.getReservaId())
                .orElseThrow(() -> new Exception("La reserva con ID " + comentarioCreateDTO.getReservaId() + " no existe."));

        // 2. Validamos que el usuario que comenta sea el dueño de la reserva.
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            throw new Exception("No puedes comentar una reserva que no te pertenece.");
        }

        // 3. Regla de Negocio: Solo se pueden comentar reservas completadas.
        if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
            throw new Exception("Solo puedes comentar reservas que ya han sido completadas.");
        }

        // 4. Regla de Negocio: Solo se puede dejar un comentario por reserva.
        if (reserva.getComentario() != null) {
            throw new Exception("Esta reserva ya tiene un comentario.");
        }

        // 5. Si todo es válido, creamos la entidad del comentario.
        ComentarioEntity nuevoComentario = comentarioMapper.toEntity(comentarioCreateDTO);
        nuevoComentario.setReserva(reserva);
        nuevoComentario.setUsuario(reserva.getUsuario());
        nuevoComentario.setAlojamiento(reserva.getAlojamiento());

        // 6. Guardamos el comentario en la BD.
        ComentarioEntity comentarioGuardado = comentarioRepository.save(nuevoComentario);

        // 7. Retornamos el DTO del comentario guardado.
        return comentarioMapper.toDTO(comentarioGuardado);
    }
}