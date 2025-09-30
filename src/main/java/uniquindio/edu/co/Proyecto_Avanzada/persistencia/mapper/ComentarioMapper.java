package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Comentario.ComentarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Auxiliares.RespuestaComentarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ComentarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper para conversiones entre ComentarioEntity y DTOs usando MapStruct

 * COMPLEJIDAD ADICIONAL:
 * - ComentarioEntity tiene relaciones con ReservaEntity, UsuarioEntity, AlojamientoEntity
 * - ComentarioDTO incluye información denormalizada de múltiples entidades
 * - Incluye respuesta del anfitrión si existe
 * - Calcula iniciales del usuario
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = {RespuestaComentarioMapper.class} // Usar mapper de respuestas
)
public interface ComentarioMapper {

    /**
     * Convierte ComentarioEntity a ComentarioDTO (LECTURA)
     *
     * MAPEOS PERSONALIZADOS:
     * - usuarioNombre, usuarioIniciales, usuarioFoto
     * - alojamientoId, alojamientoTitulo, alojamientoImagen
     * - respuesta: Se mapea automáticamente usando RespuestaComentarioMapper
     * - fechasEstadia: Se calcula desde la reserva
     *
     * MAPEO AUTOMÁTICO:
     * - id, calificacion, comentario, fecha (fechaComentario)
     */
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "usuarioIniciales", source = "usuario", qualifiedByName = "calculateIniciales")
    @Mapping(target = "usuarioFoto", source = "usuario.fotoPerfil")
    @Mapping(target = "alojamientoId", source = "alojamiento.id")
    @Mapping(target = "alojamientoTitulo", source = "alojamiento.titulo")
    @Mapping(target = "alojamientoImagen", source = "alojamiento.imagenes", qualifiedByName = "extractPrimeraImagen")
    @Mapping(target = "fecha", source = "fechaComentario")
    @Mapping(target = "respuesta", source = "respuesta")
    @Mapping(target = "fechasEstadia", source = "reserva", qualifiedByName = "formatFechasEstadia")
    ComentarioDTO toDTO(ComentarioEntity entity);

    /**
     * Convierte lista de ComentarioEntity a lista de ComentarioDTO
     */
    List<ComentarioDTO> toDTOList(List<ComentarioEntity> entities);

    /**
     * Convierte ComentarioCreateDTO a ComentarioEntity (CREAR)
     *
     * MAPEO COMPLEJO:
     * - reserva, usuario, alojamiento: Se asignan externamente en el servicio
     *
     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - reserva: Se asigna en el servicio usando reservaId
     * - usuario: Se obtiene de la reserva en el servicio
     * - alojamiento: Se obtiene de la reserva en el servicio
     * - fechaComentario: La maneja JPA
     * - respuesta: Inicialmente null
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserva", ignore = true) // Se asigna en el servicio
    @Mapping(target = "usuario", ignore = true) // Se obtiene de la reserva
    @Mapping(target = "alojamiento", ignore = true) // Se obtiene de la reserva
    @Mapping(target = "fechaComentario", ignore = true)
    @Mapping(target = "respuesta", ignore = true)
    ComentarioEntity toEntity(ComentarioCreateDTO createDTO);

    /**
     * MÉTODO AUXILIAR: Calcula iniciales del nombre del usuario
     */
    @Named("calculateIniciales")
    default String calculateIniciales(UsuarioEntity usuario) {
        if (usuario == null || usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            return "??";
        }
        String[] partes = usuario.getNombre().trim().split("\\s+");
        StringBuilder iniciales = new StringBuilder();

        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) {
                iniciales.append(partes[i].charAt(0));
            }
        }

        return iniciales.toString().toUpperCase();
    }

    /**
     * MÉTODO AUXILIAR: Extrae URL de primera imagen del alojamiento
     */
    @Named("extractPrimeraImagen")
    default String extractPrimeraImagen(List<uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento> imagenes) {
        if (imagenes == null || imagenes.isEmpty()) {
            return null;
        }
        return imagenes.stream()
                .filter(img -> Boolean.TRUE.equals(img.getEsPrincipal()))
                .findFirst()
                .map(uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ImagenAlojamiento::getUrlImagen)
                .orElse(imagenes.get(0).getUrlImagen());
    }

    /**
     * MÉTODO AUXILIAR: Formatea fechas de estadía
     */
    @Named("formatFechasEstadia")
    default String formatFechasEstadia(ReservaEntity reserva) {
        if (reserva == null || reserva.getFechaCheckIn() == null || reserva.getFechaCheckOut() == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return reserva.getFechaCheckIn().format(formatter) + " a " +
                reserva.getFechaCheckOut().format(formatter);
    }

    /**
     * MÉTODO AUXILIAR: Crea ReservaEntity con solo el ID
     */
    @Named("createReservaEntityFromId")
    default ReservaEntity createReservaEntityFromId(Long reservaId) {
        if (reservaId == null) {
            return null;
        }
        ReservaEntity reserva = new ReservaEntity();
        reserva.setId(reservaId);
        return reserva;
    }
}