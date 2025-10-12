package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.AlojamientoSummaryDTO;

import java.util.List;

// Ruta: negocio/service/FavoritoService.java
public interface FavoritoService {
    void agregarFavorito(Long usuarioId, Long alojamientoId) throws Exception;
    List<AlojamientoSummaryDTO> listarFavoritosPorUsuario(Long usuarioId) throws Exception;

    /**
     * Elimina un alojamiento de la lista de favoritos de un usuario.
     * @param usuarioId ID del usuario.
     * @param alojamientoId ID del alojamiento a eliminar.
     * @throws Exception Si el usuario o el alojamiento no se encuentran.
     */
    void quitarFavorito(Long usuarioId, Long alojamientoId) throws Exception;
}