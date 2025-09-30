package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Reserva.ReservaDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.ReservaEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.AlojamientoEntity;
import org.mapstruct.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Mapper para conversiones entre ReservaEntity y DTOs usando MapStruct

 * COMPLEJIDAD ADICIONAL:
 * - ReservaEntity tiene relaciones con UsuarioEntity y AlojamientoEntity
 * - ReservaDTO incluye información denormalizada de ambos
 * - Necesita calcular número de noches
 * - Incluye información del huésped para anfitriones
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReservaMapper {

    /**
     * Convierte ReservaEntity a ReservaDTO (LECTURA)

     * MAPEOS PERSONALIZADOS:
     * - alojamientoId, alojamientoTitulo, alojamientoImagen, alojamientoCiudad
     * - huespedNombre, huespedEmail, huespedTelefono
     * - noches: Se calcula entre fechas
     * - precioPorNoche: Se extrae del alojamiento
     * - puedeCancelar, puedeComentary: Se calculan en el servicio

     * MAPEO AUTOMÁTICO:
     * - id, fechaCheckIn, fechaCheckOut, numHuespedes, precioTotal, estado,
     *   fechaReserva, fechaCancelacion, motivoCancelacion
     */
    @Mapping(target = "alojamientoId", source = "alojamiento.id")
    @Mapping(target = "alojamientoTitulo", source = "alojamiento.titulo")
    @Mapping(target = "alojamientoImagen", source = "alojamiento.imagenes", qualifiedByName = "extractPrimeraImagen")
    @Mapping(target = "alojamientoCiudad", source = "alojamiento.ciudad")
    @Mapping(target = "numHuespedes", source = "numeroHuespedes")
    @Mapping(target = "noches", source = "entity", qualifiedByName = "calculateNoches")
    @Mapping(target = "precioPorNoche", source = "alojamiento.precioPorNoche")
    @Mapping(target = "huespedNombre", source = "usuario.nombre")
    @Mapping(target = "huespedEmail", source = "usuario.email")
    @Mapping(target = "huespedTelefono", source = "usuario.telefono")
    @Mapping(target = "puedeCancelar", ignore = true) // Se calcula en servicio
    @Mapping(target = "puedeComentary", ignore = true) // Se calcula en servicio
    ReservaDTO toDTO(ReservaEntity entity);

    /**
     * Convierte lista de ReservaEntity a lista de ReservaDTO
     */
    List<ReservaDTO> toDTOList(List<ReservaEntity> entities);

    /**
     * Convierte ReservaCreateDTO a ReservaEntity (CREAR)

     * MAPEO COMPLEJO:
     * - usuario: Se asigna externamente en el servicio (usuario autenticado)
     * - alojamiento: Se crea referencia con solo el ID
     * - precioTotal: Se calcula en el servicio

     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - usuario: Se asigna en el servicio
     * - alojamiento: Se asigna en el servicio usando alojamientoId
     * - precioTotal: Se calcula en el servicio
     * - estado: Por defecto PENDIENTE
     * - fechaReserva: La maneja JPA
     * - fechaCancelacion, motivoCancelacion, comentario: Inicialmente null
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true) // Se asigna en el servicio
    @Mapping(target = "alojamiento", ignore = true) // Se asigna en el servicio
    @Mapping(target = "numeroHuespedes", source = "numHuespedes")
    @Mapping(target = "precioTotal", ignore = true) // Se calcula en el servicio
    @Mapping(target = "estado", ignore = true) // Por defecto PENDIENTE
    @Mapping(target = "fechaReserva", ignore = true)
    @Mapping(target = "fechaCancelacion", ignore = true)
    @Mapping(target = "motivoCancelacion", ignore = true)
    @Mapping(target = "comentario", ignore = true)
    ReservaEntity toEntity(ReservaCreateDTO createDTO);

    /**
     * MÉTODO AUXILIAR: Calcula número de noches entre fechas
     */
    @Named("calculateNoches")
    default Integer calculateNoches(ReservaEntity entity) {
        if (entity.getFechaCheckIn() == null || entity.getFechaCheckOut() == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(entity.getFechaCheckIn(), entity.getFechaCheckOut());
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
     * MÉTODO AUXILIAR: Crea UsuarioEntity con solo el ID
     */
    @Named("createUsuarioEntityFromId")
    default UsuarioEntity createUsuarioEntityFromId(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        return usuario;
    }

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