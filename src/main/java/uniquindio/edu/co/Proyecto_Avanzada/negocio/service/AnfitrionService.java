// Ruta: negocio/service/AnfitrionService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Anfitrion.MetricasAnfitrionDTO;

public interface AnfitrionService {
    MetricasAnfitrionDTO obtenerMetricas(Long anfitrionId);
}