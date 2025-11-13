package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.CityDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.FeaturedAccommodationDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Public.StatisticsDTO;

import java.util.List;

/**
 * Servicio para endpoints públicos (no requieren autenticación)
 */
public interface PublicService {

    /**
     * Obtiene hasta 6 alojamientos destacados
     * @return Lista de alojamientos destacados
     */
    List<FeaturedAccommodationDTO> obtenerAlojamientosDestacados();

    /**
     * Obtiene estadísticas generales de la plataforma
     * @return Estadísticas del sistema
     */
    StatisticsDTO obtenerEstadisticas();

    /**
     * Obtiene lista de ciudades con alojamientos disponibles
     * @return Lista de ciudades con cantidad de alojamientos
     */
    List<CityDTO> obtenerCiudades();
}
