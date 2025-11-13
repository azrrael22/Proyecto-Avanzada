package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.CityDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.FeaturedAccommodationDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.StatisticsDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoReserva;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.AlojamientoRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ComentarioRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ReservaRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public List<FeaturedAccommodationDTO> obtenerAlojamientosDestacados() {
        // Crear paginación para limitar a 6 resultados
        Pageable pageable = PageRequest.of(0, 6);

        // Buscar alojamientos activos ordenados por calificación
        List<AlojamientoEntity> alojamientos = alojamientoRepository
                .findTop6ByEstadoOrderByCalificacionPromedioDesc(EstadoAlojamiento.ACTIVO, pageable);

        // Convertir a DTO
        return alojamientos.stream()
                .map(this::convertirAFeaturedDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StatisticsDTO obtenerEstadisticas() {
        // Contar alojamientos activos
        Long totalAlojamientos = alojamientoRepository.countByEstado(EstadoAlojamiento.ACTIVO);

        // Contar usuarios totales
        Long totalUsuarios = usuarioRepository.count();

        // Contar ciudades distintas con alojamientos activos
        Long totalCiudades = alojamientoRepository.countDistinctCiudadesByEstado(EstadoAlojamiento.ACTIVO);

        // Contar reservas completadas
        Long totalReservas = reservaRepository.countByEstado(EstadoReserva.COMPLETADA);

        return StatisticsDTO.builder()
                .totalAlojamientos(totalAlojamientos)
                .totalUsuarios(totalUsuarios)
                .totalCiudades(totalCiudades)
                .totalReservas(totalReservas)
                .build();
    }

    @Override
    public List<CityDTO> obtenerCiudades() {
        // Obtener ciudades con alojamientos activos
        return alojamientoRepository.findCiudadesWithActiveAccommodations();
    }

    /**
     * Convierte AlojamientoEntity a FeaturedAccommodationDTO
     */
    private FeaturedAccommodationDTO convertirAFeaturedDTO(AlojamientoEntity entity) {
        // Extraer imagen principal
        String imagenPrincipal = entity.getImagenes().stream()
                .filter(ImagenAlojamiento::getEsPrincipal)
                .map(ImagenAlojamiento::getUrlImagen)
                .findFirst()
                .orElse("https://via.placeholder.com/800x600/d9b777/ffffff?text=Alojamiento");

        // Calcular calificación promedio (si hay comentarios)
        Double calificacion = comentarioRepository.findAverageCalificacionByAlojamientoId(entity.getId())
                .orElse(0.0);

        return FeaturedAccommodationDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .ciudad(entity.getCiudad())
                .precioPorNoche(entity.getPrecioPorNoche())
                .imagenPrincipal(imagenPrincipal)
                .calificacionPromedio(calificacion)
                .destacado(true)
                .build();
    }
}