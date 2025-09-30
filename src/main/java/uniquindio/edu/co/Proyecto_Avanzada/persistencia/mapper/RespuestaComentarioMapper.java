package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.RespuestaComentarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.RespuestaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RespuestaComentarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ComentarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre RespuestaComentarioEntity y DTOs

 * SIMPLICIDAD:
 * - Mapper relativamente simple con pocas relaciones
 * - Se usa principalmente en conjunto con ComentarioMapper
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RespuestaComentarioMapper {

    /**
     * Convierte RespuestaComentarioEntity a RespuestaComentarioDTO (LECTURA)

     * MAPEOS PERSONALIZADOS:
     * - anfitrion: Se extrae el nombre del anfitrión
     * - fecha: Se mapea desde fechaRespuesta

     * MAPEO AUTOMÁTICO:
     * - id, respuesta
     */
    @Mapping(target = "anfitrion", source = "anfitrion.nombre")
    @Mapping(target = "fecha", source = "fechaRespuesta")
    RespuestaComentarioDTO toDTO(RespuestaComentarioEntity entity);

    /**
     * Convierte lista de RespuestaComentarioEntity a lista de RespuestaComentarioDTO
     */
    List<RespuestaComentarioDTO> toDTOList(List<RespuestaComentarioEntity> entities);

    /**
     * Convierte RespuestaCreateDTO a RespuestaComentarioEntity (CREAR)

     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - comentario: Se asigna externamente en el servicio
     * - anfitrion: Se asigna externamente en el servicio (usuario autenticado)
     * - fechaRespuesta: La maneja JPA
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comentario", ignore = true) // Se asigna en el servicio
    @Mapping(target = "anfitrion", ignore = true) // Se asigna en el servicio
    @Mapping(target = "fechaRespuesta", ignore = true)
    RespuestaComentarioEntity toEntity(RespuestaCreateDTO createDTO);

    /**
     * METODO AUXILIAR: Crea ComentarioEntity con solo el ID
     */
    @Named("createComentarioEntityFromId")
    default ComentarioEntity createComentarioEntityFromId(Long comentarioId) {
        if (comentarioId == null) {
            return null;
        }
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setId(comentarioId);
        return comentario;
    }

    /**
     * MÉTODO AUXILIAR: Crea UsuarioEntity con solo el ID
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