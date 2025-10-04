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
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginRequestDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginResponseDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioUpdateDTO;

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

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception {
        // 1. Buscar al usuario por su email en la base de datos
        UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new Exception("El correo o la contraseña son incorrectos."));

        // 2. (IMPORTANTE) Validar la contraseña
        // En un proyecto real, la contraseña de la BD está encriptada.
        // Se usaría un PasswordEncoder para compararlas:
        // if (!passwordEncoder.matches(loginRequestDTO.getPassword(), usuario.getContraseniaHash())) {
        //     throw new Exception("El correo o la contraseña son incorrectos.");
        // }
        // Para este avance, haremos una comparación simple de texto:
        if (!loginRequestDTO.getPassword().equals(usuario.getContraseniaHash())) {
            throw new Exception("El correo o la contraseña son incorrectos.");
        }

        // 3. (IMPORTANTE) Generar un Token JWT (JSON Web Token)
        // La generación del token es un proceso más complejo que requiere una librería y configuración.
        // Para este avance, simularemos un token. Más adelante lo podrás reemplazar.
        String token = "TOKEN_JWT_SIMULADO_PARA_" + usuario.getEmail();

        // 4. Convertir la entidad del usuario a un DTO para la respuesta
        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);

        // 5. Construir y devolver la respuesta de login
        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expira(3600) // 1 hora de validez (en segundos)
                .usuario(usuarioDTO)
                .build();
    }

    @Override
    public UsuarioDTO actualizarPerfil(Long usuarioId, UsuarioUpdateDTO updateDTO) throws Exception {
        // 1. Buscamos al usuario que se quiere actualizar.
        UsuarioEntity usuarioExistente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new Exception("El usuario con ID " + usuarioId + " no fue encontrado."));

        // 2. Usamos el mapper para aplicar los cambios del DTO a la entidad.
        usuarioMapper.updateEntityFromDTO(updateDTO, usuarioExistente);

        // 3. Guardamos la entidad actualizada.
        UsuarioEntity usuarioActualizado = usuarioRepository.save(usuarioExistente);

        // 4. Retornamos el DTO actualizado.
        return usuarioMapper.toDTO(usuarioActualizado);
    }
}