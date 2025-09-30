package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Alojamiento.*;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.ImagenAlojamientoDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversiones entre AlojamientoEntity y DTOs usando MapStruct

 * COMPLEJIDAD ADICIONAL:
 * - AlojamientoEntity tiene relación con UsuarioEntity (anfitrión)
 * - AlojamientoDTO incluye información denormalizada del anfitrión
 * - Necesitamos mapear anfitrionId ↔ UsuarioEntity
 * - Incluye lista de imágenes con información completa
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = {ImagenAlojamientoMapper.class} // Usar mapper de imágenes
)
public interface AlojamientoMapper {

    /**
     * Convierte AlojamientoEntity a AlojamientoDTO (LECTURA COMPLETA)

     * MAPEOS PERSONALIZADOS:
     * - anfitrionId: Se extrae de anfitrion.id
     * - anfitrionNombre: Se extrae de anfitrion.nombre
     * - anfitrionFoto: Se extrae de anfitrion.fotoPerfil
     * - calificacionPromedio: Se calcula externamente en el servicio
     * - totalComentarios: Se calcula externamente en el servicio
     * - totalReservas: Se calcula externamente en el servicio

     * MAPEO AUTOMÁTICO:
     * - id, titulo, descripcion, ciudad, direccionCompleta, precioPorNoche,
     *   capacidadMaxima, tipo, servicios, estado, fechaCreacion, fechaActualizacion
     */
    @Mapping(target = "anfitrionId", source = "anfitrion.id")
    @Mapping(target = "anfitrionNombre", source = "anfitrion.nombre")
    @Mapping(target = "anfitrionFoto", source = "anfitrion.fotoPerfil")
    @Mapping(target = "imagenes", source = "imagenes")
    @Mapping(target = "calificacionPromedio", ignore = true) // Se calcula en servicio
    @Mapping(target = "totalComentarios", ignore = true) // Se calcula en servicio
    @Mapping(target = "totalReservas", ignore = true) // Se calcula en servicio
    AlojamientoDTO toDTO(AlojamientoEntity entity);

    /**
     * Convierte lista de AlojamientoEntity a lista de AlojamientoDTO
     */
    List<AlojamientoDTO> toDTOList(List<AlojamientoEntity> entities);

    /**
     * Convierte AlojamientoEntity a AlojamientoSummaryDTO (LECTURA RESUMIDA)

     * MAPEOS PERSONALIZADOS:
     * - imagenPrincipal: Se extrae de la lista de imágenes (primera con esPrincipal=true)
     * - calificacionPromedio: Se calcula externamente en el servicio
     */
    @Mapping(target = "imagenPrincipal", source = "imagenes", qualifiedByName = "extractImagenPrincipal")
    @Mapping(target = "calificacionPromedio", ignore = true) // Se calcula en servicio
    AlojamientoSummaryDTO toSummaryDTO(AlojamientoEntity entity);

    /**
     * Convierte lista de AlojamientoEntity a lista de AlojamientoSummaryDTO
     */
    List<AlojamientoSummaryDTO> toSummaryDTOList(List<AlojamientoEntity> entities);

    /**
     * Convierte AlojamientoCreateDTO a AlojamientoEntity (CREAR)

     * MAPEO COMPLEJO:
     * - anfitrion: Se asigna externamente en el servicio (usuario autenticado)

     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - anfitrion: Se asigna en el servicio
     * - estado: Por defecto ACTIVO
     * - fechaCreacion/fechaActualizacion: Los maneja JPA
     * - imagenes, reservas, comentarios: Relaciones manejadas externamente
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "anfitrion", ignore = true) // Se asigna en el servicio
    @Mapping(target = "estado", ignore = true) // Por defecto ACTIVO
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    AlojamientoEntity toEntity(AlojamientoCreateDTO createDTO);

    /**
     * Actualiza AlojamientoEntity existente con datos de AlojamientoUpdateDTO

     * NOTA IMPORTANTE:
     * - NO actualizamos anfitrion (alojamiento no cambia de dueño)
     * - NO actualizamos tipo, ciudad, direccionCompleta (datos estructurales)
     * - Estrategia IGNORE para valores null (actualización parcial)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "anfitrion", ignore = true) // No se puede cambiar dueño
    @Mapping(target = "tipo", ignore = true) // No se puede cambiar tipo
    @Mapping(target = "ciudad", ignore = true) // No se puede cambiar ciudad
    @Mapping(target = "direccionCompleta", ignore = true) // No se puede cambiar dirección
    @Mapping(target = "estado", ignore = true) // Se cambia por endpoint específico
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AlojamientoUpdateDTO updateDTO, @MappingTarget AlojamientoEntity entity);

    /**
     * MÉTODO AUXILIAR: Extrae URL de imagen principal
     *
     * @Named: Permite referenciar este método en otros mapeos
     */
    @Named("extractImagenPrincipal")
    default String extractImagenPrincipal(List<ImagenAlojamiento> imagenes) {
        if (imagenes == null || imagenes.isEmpty()) {
            return null;
        }
        return imagenes.stream()
                .filter(img -> Boolean.TRUE.equals(img.getEsPrincipal()))
                .findFirst()
                .map(ImagenAlojamiento::getUrlImagen)
                .orElse(imagenes.get(0).getUrlImagen()); // Si no hay principal, tomar la primera
    }

    /**
     * MÉTODO AUXILIAR: Crea UsuarioEntity con solo el ID

     * ¿POR QUÉ ESTE MÉTODO?
     * - Cuando creamos un alojamiento, solo tenemos el anfitrionId
     * - No necesitamos cargar todo el usuario de BD, solo la referencia
     * - JPA manejará la relación correctamente con solo el ID
     */
    @Named("createAnfitrionEntityFromId")
    default UsuarioEntity createAnfitrionEntityFromId(Long anfitrionId) {
        if (anfitrionId == null) {
            return null;
        }
        UsuarioEntity anfitrion = new UsuarioEntity();
        anfitrion.setId(anfitrionId);
        return anfitrion;
    }
}