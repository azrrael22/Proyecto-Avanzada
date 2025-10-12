package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.AlojamientoMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.AlojamientoRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

// Ruta: negocio/service/FavoritoServiceImpl.java
@Service
@RequiredArgsConstructor
public class FavoritoServiceImpl implements FavoritoService {

    private final UsuarioRepository usuarioRepo;
    private final AlojamientoRepository alojamientoRepo;
    private final AlojamientoMapper alojamientoMapper;

    @Override
    @Transactional // Importante para manejar relaciones complejas
    public void agregarFavorito(Long usuarioId, Long alojamientoId) throws Exception {
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        AlojamientoEntity alojamiento = alojamientoRepo.findById(alojamientoId)
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        // Añadimos el alojamiento a la lista de favoritos del usuario
        usuario.getAlojamientosFavoritos().add(alojamiento);

        // No es necesario guardar (save), @Transactional se encarga de ello al final del método.
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlojamientoSummaryDTO> listarFavoritosPorUsuario(Long usuarioId) throws Exception {
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        return alojamientoMapper.toSummaryDTOList(new ArrayList<>(usuario.getAlojamientosFavoritos()));
    }

    @Override
    @Transactional // Importante para que los cambios en la lista se guarden en la BD
    public void quitarFavorito(Long usuarioId, Long alojamientoId) throws Exception {
        // 1. Buscamos al usuario.
        UsuarioEntity usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // 2. Buscamos el alojamiento que se va a eliminar de la lista.
        AlojamientoEntity alojamiento = alojamientoRepo.findById(alojamientoId)
                .orElseThrow(() -> new Exception("Alojamiento no encontrado"));

        // 3. Removemos el alojamiento de la colección de favoritos del usuario.
        //    Si el alojamiento no estaba en la lista, el método simplemente no hace nada.
        usuario.getAlojamientosFavoritos().remove(alojamiento);

        // Al finalizar el método, @Transactional se encargará de actualizar la base de datos.
    }
}