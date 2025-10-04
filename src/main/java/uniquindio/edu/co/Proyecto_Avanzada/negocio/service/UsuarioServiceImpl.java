// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/UsuarioServiceImpl.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.UsuarioMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.RolRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RolEntity;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Nuestro DAO para usuarios

    @Autowired
    private RolRepository rolRepository; // Nuestro DAO para roles

    @Autowired
    private UsuarioMapper usuarioMapper; // Nuestro convertidor

    // NOTA: Deberíamos inyectar un PasswordEncoder para seguridad real
    // private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO registrarUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception {
        // 1. Verificar si el email ya existe en la base de datos
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new Exception("El email " + usuarioCreateDTO.getEmail() + " ya está registrado.");
        }

        // 2. Convertir el DTO de creación a una Entidad de Usuario
        UsuarioEntity nuevoUsuario = usuarioMapper.toEntity(usuarioCreateDTO);

        // 3. Asignar el rol por defecto "USUARIO"
        // Buscamos el rol en la BD. Si no existe, lanzamos un error.
        RolEntity rolUsuario = rolRepository.findByNombreRol("USUARIO")
                .orElseThrow(() -> new Exception("El rol 'USUARIO' no se encontró en la base de datos."));
        nuevoUsuario.setRol(rolUsuario);

        // 4. (IMPORTANTE) En un proyecto real, aquí deberíamos encriptar la contraseña
        // String passwordEncriptada = passwordEncoder.encode(usuarioCreateDTO.getPassword());
        // nuevoUsuario.setContraseniaHash(passwordEncriptada);
        // Por ahora, la guardamos como texto plano como en el DTO.

        // 5. Guardar el nuevo usuario en la base de datos usando el repositorio
        UsuarioEntity usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // 6. Convertir la entidad guardada a un DTO de respuesta y devolverlo
        return usuarioMapper.toDTO(usuarioGuardado);
    }
}