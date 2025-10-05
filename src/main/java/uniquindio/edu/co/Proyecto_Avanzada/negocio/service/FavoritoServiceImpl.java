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
}