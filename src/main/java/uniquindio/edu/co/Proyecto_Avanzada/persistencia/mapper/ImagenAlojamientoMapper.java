package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.ImagenAlojamientoDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre ImagenAlojamiento y ImagenAlojamientoDTO

 * SIMPLICIDAD:
 * - Mapper directo sin relaciones complejas
 * - Se usa principalmente en conjunto con AlojamientoMapper
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ImagenAlojamientoMapper {

    /**
     * Convierte ImagenAlojamiento a ImagenAlojamientoDTO (LECTURA)

     * MAPEOS PERSONALIZADOS:
     * - url: Se mapea desde urlImagen

     * MAPEO AUTOMÁTICO:
     * - id, esPrincipal, orden, fechaSubida
     */
    @Mapping(target = "url", source = "urlImagen")
    ImagenAlojamientoDTO toDTO(ImagenAlojamiento entity);

    /**
     * Convierte lista de ImagenAlojamiento a lista de ImagenAlojamientoDTO
     */
    List<ImagenAlojamientoDTO> toDTOList(List<ImagenAlojamiento> entities);

    /**
     * Convierte ImagenAlojamientoDTO a ImagenAlojamiento (CREAR)

     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - alojamiento: Se asigna externamente en el servicio
     * - fechaSubida: La maneja JPA
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "urlImagen", source = "url")
    @Mapping(target = "alojamiento", ignore = true) // Se asigna en el servicio
    @Mapping(target = "fechaSubida", ignore = true)
    ImagenAlojamiento toEntity(ImagenAlojamientoDTO dto);

    /**
     * MÉTODO AUXILIAR: Crea AlojamientoEntity con solo el ID
     */
    @Named("createAlojamientoEntityFromId")
    default AlojamientoEntity createAlojamientoEntityFromId(Long alojamientoId) {
        if (alojamientoId == null) {
            return null;
        }
        AlojamientoEntity alojamiento = new AlojamientoEntity();
        alojamiento.setId(alojamientoId);
        return alojamiento;
    }
}