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

@Service
public class AlojamientoServiceImpl implements AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository; // DAO para alojamientos [cite: 3379]

    @Autowired
    private UsuarioRepository usuarioRepository; // DAO para usuarios [cite: 3508]

    @Autowired
    private AlojamientoMapper alojamientoMapper; // Convertidor de DTO a Entidad [cite: 3294]

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
}