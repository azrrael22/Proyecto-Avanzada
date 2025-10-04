// Ruta: negocio/service/AnfitrionServiceImpl.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Anfitrion.MetricasAnfitrionDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.enums.EstadoAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.AlojamientoRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ComentarioRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.ReservaRepository;

@Service
@RequiredArgsConstructor // Inyección de dependencias por constructor, más moderno que @Autowired
public class AnfitrionServiceImpl implements AnfitrionService {

    private final AlojamientoRepository alojamientoRepo;
    private final ReservaRepository reservaRepo;
    private final ComentarioRepository comentarioRepo;

    @Override
    public MetricasAnfitrionDTO obtenerMetricas(Long anfitrionId) {
        long totalAlojamientos = alojamientoRepo.countByAnfitrion_IdAndEstadoNot(anfitrionId, EstadoAlojamiento.ELIMINADO);
        long totalReservas = reservaRepo.countByAlojamiento_Anfitrion_Id(anfitrionId);
        Double calificacionPromedio = comentarioRepo.findAverageCalificacionByAnfitrionId(anfitrionId);

        return MetricasAnfitrionDTO.builder()
                .totalAlojamientos(totalAlojamientos)
                .totalReservasRecibidas(totalReservas)
                .calificacionPromedio(calificacionPromedio != null ? calificacionPromedio : 0.0)
                .build();
    }
}