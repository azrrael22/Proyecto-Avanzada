// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/AlojamientoServiceImpl.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.AlojamientoMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.AlojamientoRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoUpdateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ReservaRepository;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import java.util.List;

@Service
public class AlojamientoServiceImpl implements AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository; // DAO para alojamientos [cite: 3379]

    @Autowired
    private UsuarioRepository usuarioRepository; // DAO para usuarios [cite: 3508]

    @Autowired
    private AlojamientoMapper alojamientoMapper; // Convertidor de DTO a Entidad [cite: 3294]

    @Autowired
    private ReservaRepository reservaRepository; //DAO de Reservas

    @Override
    public AlojamientoDTO crearAlojamiento(AlojamientoCreateDTO alojamientoCreateDTO, Long anfitrionId) throws Exception {
        // 1. Validamos que el anfitrión que va a crear el alojamiento realmente exista.
        UsuarioEntity anfitrion = usuarioRepository.findById(anfitrionId)
                .orElseThrow(() -> new Exception("El anfitrión con ID " + anfitrionId + " no fue encontrado."));

        // 2. Convertimos el DTO que llega con los datos a una entidad para guardarla en la BD.
        AlojamientoEntity nuevoAlojamiento = alojamientoMapper.toEntity(alojamientoCreateDTO);

        // 3. Establecemos la relación: le decimos al nuevo alojamiento quién es su dueño (anfitrión).
        nuevoAlojamiento.setAnfitrion(anfitrion);

        // 4. Guardamos el nuevo alojamiento en la base de datos.
        AlojamientoEntity alojamientoGuardado = alojamientoRepository.save(nuevoAlojamiento);

        // 5. Convertimos la entidad que acabamos de guardar a un DTO de respuesta para enviarlo de vuelta.
        return alojamientoMapper.toDTO(alojamientoGuardado);
    }

    @Override
    public List<AlojamientoSummaryDTO> listarAlojamientosPorAnfitrion(Long anfitrionId) {
        // ANTES: Buscábamos todos sin filtrar.
        // List<AlojamientoEntity> alojamientos = alojamientoRepository.findByAnfitrion_Id(anfitrionId);

        // AHORA: Usamos el nuevo método para excluir los eliminados.
        List<AlojamientoEntity> alojamientos = alojamientoRepository.findByAnfitrion_IdAndEstadoNot(
                anfitrionId,
                EstadoAlojamiento.ELIMINADO
        );

        // El resto del método no cambia.
        return alojamientoMapper.toSummaryDTOList(alojamientos);
    }

    @Override
    public AlojamientoDTO actualizarAlojamiento(Long alojamientoId, AlojamientoUpdateDTO alojamientoUpdateDTO, Long anfitrionId) throws Exception {
        // 1. Buscamos el alojamiento en la base de datos por su ID.
        AlojamientoEntity alojamientoExistente = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new Exception("El alojamiento con ID " + alojamientoId + " no fue encontrado."));

        // 2. ¡VALIDACIÓN DE SEGURIDAD! Verificamos que el anfitrión que hace la petición
        //    sea el verdadero dueño del alojamiento.
        if (!alojamientoExistente.getAnfitrion().getId().equals(anfitrionId)) {
            throw new Exception("No tienes permiso para editar este alojamiento.");
        }

        // 3. Usamos el mapper para actualizar la entidad existente con los datos del DTO.
        //    MapStruct es lo suficientemente inteligente para solo actualizar los campos que no son nulos.
        alojamientoMapper.updateEntityFromDTO(alojamientoUpdateDTO, alojamientoExistente);

        // 4. Guardamos la entidad actualizada en la base de datos.
        AlojamientoEntity alojamientoActualizado = alojamientoRepository.save(alojamientoExistente);

        // 5. Convertimos la entidad final a un DTO y la retornamos.
        return alojamientoMapper.toDTO(alojamientoActualizado);
    }

    @Override
    public void eliminarAlojamiento(Long alojamientoId, Long anfitrionId) throws Exception {
        // 1. Buscamos el alojamiento que se quiere eliminar.
        AlojamientoEntity alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new Exception("El alojamiento con ID " + alojamientoId + " no fue encontrado."));

        // 2. Verificamos que quien lo intenta borrar sea el dueño.
        if (!alojamiento.getAnfitrion().getId().equals(anfitrionId)) {
            throw new Exception("No tienes permiso para eliminar este alojamiento.");
        }

        // 3. Verificamos la regla de negocio: ¿Tiene reservas futuras?
        //    Usamos el método que ya existía en el ReservaRepository.
        if (reservaRepository.hasFutureReservations(alojamientoId)) {
            throw new Exception("No se puede eliminar un alojamiento con reservas futuras activas.");
        }

        // 4. Si pasa todas las validaciones, realizamos el borrado lógico.
        //    No lo borramos de la BD, solo cambiamos su estado.
        alojamiento.setEstado(EstadoAlojamiento.ELIMINADO);

        // 5. Guardamos el cambio en la base de datos.
        alojamientoRepository.save(alojamiento);
    }
}