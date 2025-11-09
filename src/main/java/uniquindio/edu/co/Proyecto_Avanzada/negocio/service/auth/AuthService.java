package uniquindio.edu.co.Proyecto_Avanzada.negocio.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.edu.co.Proyecto_Avanzada.configuracion.seguridad.JwtUtil;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginRequestDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginResponseDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.*;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.RolEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.mapper.UsuarioMapper;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.RolRepository;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;

/**
 * Servicio de autenticación con JWT real
 * ADAPTADO PARA: Roles guardados en tabla (no enum)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;  // 🔥 NUEVO: Necesario para buscar roles
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Login de usuario con JWT real
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws Exception {

        // 1. Autenticar al usuario con Spring Security
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new Exception("Credenciales inválidas: " + e.getMessage());
        }

        // 2. Si la autenticación fue exitosa, buscar el usuario
        UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // 3. 🔥 CAMBIO: Extraer el nombre del rol desde RolEntity
        String nombreRol = usuario.getRol() != null
                ? usuario.getRol().getNombreRol()
                : "USUARIO";  // Rol por defecto

        // 4. Generar el token JWT real
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getId(),
                nombreRol  // Ahora es String, no enum
        );

        // 5. Convertir la entidad a DTO
        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);

        // 6. Construir y retornar la respuesta
        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expira(3600) // 1 hora en segundos (ajustar según tu configuración)
                .usuario(usuarioDTO)
                .build();
    }

    /**
     * Registro de nuevo usuario
     */
    public UsuarioDTO register(UsuarioCreateDTO usuarioCreateDTO) throws Exception {

        // 1. Verificar si el email ya existe
        if (usuarioRepository.findByEmail(usuarioCreateDTO.getEmail()).isPresent()) {
            throw new Exception("El email ya está registrado");
        }

        // 2. Crear la nueva entidad de usuario
        UsuarioEntity nuevoUsuario = new UsuarioEntity();
        nuevoUsuario.setEmail(usuarioCreateDTO.getEmail());
        nuevoUsuario.setNombre(usuarioCreateDTO.getNombre());
        //nuevoUsuario.setApellido(usuarioCreateDTO.getApellido());
        nuevoUsuario.setTelefono(usuarioCreateDTO.getTelefono());

        // 3. Encriptar la contraseña con BCrypt
        String passwordEncriptada = passwordEncoder.encode(usuarioCreateDTO.getPassword());
        nuevoUsuario.setContraseniaHash(passwordEncriptada);

        // 4. Buscar el rol en la base de datos
        String nombreRolSolicitado = (usuarioCreateDTO.getRol() != null)
                ? usuarioCreateDTO.getRol()
                : "USUARIO";  // Por defecto USUARIO

        RolEntity rolEntity = rolRepository.findByNombreRol(nombreRolSolicitado)
                .orElseThrow(() -> new Exception(
                        "El rol '" + nombreRolSolicitado + "' no existe en la base de datos. " +
                                "Por favor, asegúrate de que el rol esté creado."
                ));

        nuevoUsuario.setRol(rolEntity);

        // 5. Guardar el usuario en la base de datos
        UsuarioEntity usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // 6. Convertir a DTO y retornar
        return usuarioMapper.toDTO(usuarioGuardado);
    }
}