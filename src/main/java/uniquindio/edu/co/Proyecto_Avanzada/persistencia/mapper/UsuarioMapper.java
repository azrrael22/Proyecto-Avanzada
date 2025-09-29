package uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioUpdateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RolEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre UsuarioEntity y DTOs usando MapStruct
 * COMPLEJIDAD ADICIONAL:
 * - UsuarioEntity tiene relación con RolEntity
 * - UsuarioDTO incluye información denormalizada del rol (rol como String)
 * - Necesitamos mapear rol String ↔ RolEntity
 * - Password solo se incluye en creación, nunca en respuestas
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {

    /**
     * Convierte UsuarioEntity a UsuarioDTO (LECTURA)

     * MAPEOS PERSONALIZADOS:
     * - rol: Se extrae de rol.nombreRol
     * - contraseniaHash: NO se mapea (sensible)

     * MAPEO AUTOMÁTICO:
     * - id, nombre, email, telefono, fechaNacimiento, fotoPerfil, estado,
     *   fechaRegistro, fechaUltimaActualizacion
     */
    @Mapping(target = "rol", source = "rol.nombreRol")
    @Mapping(target = "ultimoAcceso", source = "fechaUltimaActualizacion")
    UsuarioDTO toDTO(UsuarioEntity entity);

    /**
     * Convierte lista de UsuarioEntity a lista de UsuarioDTO
     */
    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);

    /**
     * Convierte UsuarioCreateDTO a UsuarioEntity (CREAR)

     * MAPEO COMPLEJO:
     * - password se mapea a contraseniaHash (será hasheado en el servicio)
     * - rol se asigna automáticamente como "USUARIO" en el servicio

     * CAMPOS IGNORADOS:
     * - id: Se genera automáticamente
     * - rol: Se asigna por defecto en el servicio
     * - estado: Por defecto ACTIVO
     * - fechas: Las maneja JPA
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contraseniaHash", source = "password")
    @Mapping(target = "rol", ignore = true) // Se asigna en el servicio
    @Mapping(target = "estado", ignore = true) // Por defecto ACTIVO
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaUltimaActualizacion", ignore = true)
    @Mapping(target = "alojamientos", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "codigosRecuperacion", ignore = true)
    @Mapping(target = "auditoriaComoUsuario", ignore = true)
    @Mapping(target = "auditoriaComoAdmin", ignore = true)
    @Mapping(target = "respuestas", ignore = true)
    UsuarioEntity toEntity(UsuarioCreateDTO createDTO);

    /**
     * Actualiza UsuarioEntity existente con datos de UsuarioUpdateDTO

     * NOTA IMPORTANTE:
     * - NO actualizamos email, rol, estado (campos críticos)
     * - NO actualizamos password (se cambia por endpoint separado)
     * - Estrategia IGNORE para valores null (actualización parcial)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true) // Email no se puede cambiar
    @Mapping(target = "contraseniaHash", ignore = true) // Password por endpoint separado
    @Mapping(target = "rol", ignore = true) // Solo admin puede cambiar rol
    @Mapping(target = "estado", ignore = true) // Solo admin puede cambiar estado
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "fechaUltimaActualizacion", ignore = true)
    @Mapping(target = "alojamientos", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "codigosRecuperacion", ignore = true)
    @Mapping(target = "auditoriaComoUsuario", ignore = true)
    @Mapping(target = "auditoriaComoAdmin", ignore = true)
    @Mapping(target = "respuestas", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO updateDTO, @MappingTarget UsuarioEntity entity);

    /**
     * Convierte UsuarioDTO a UsuarioEntity (para casos especiales)
     */
    @Mapping(target = "contraseniaHash", ignore = true) // No incluir password
    @Mapping(target = "rol", source = "rol", qualifiedByName = "createRolEntityFromString")
    @Mapping(target = "alojamientos", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "codigosRecuperacion", ignore = true)
    @Mapping(target = "auditoriaComoUsuario", ignore = true)
    @Mapping(target = "auditoriaComoAdmin", ignore = true)
    @Mapping(target = "respuestas", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    /**
     * MÉTODO AUXILIAR: Crea RolEntity con solo el nombre

     * @Named: Permite referenciar este método en otros mapeos
     */
    @Named("createRolEntityFromString")
    default RolEntity createRolEntityFromString(String nombreRol) {
        if (nombreRol == null) {
            return null;
        }
        RolEntity rol = new RolEntity();
        rol.setNombreRol(nombreRol);
        return rol;
    }

    /**
     * METODO AUXILIAR: Extrae el nombre del RolEntity
     */
    @Named("extractRolNameFromEntity")
    default String extractRolNameFromEntity(RolEntity rolEntity) {
        return rolEntity != null ? rolEntity.getNombreRol() : null;
    }
}
