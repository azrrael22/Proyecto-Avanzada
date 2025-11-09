package uniquindio.edu.co.Proyecto_Avanzada.configuracion.seguridad;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.entity.UsuarioEntity;
import uniquindio.edu.co.Proyecto_Avanzada.persistencia.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio personalizado para cargar usuarios desde la base de datos
 * ADAPTADO PARA: Roles guardados en tabla (no enum)
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Buscar el usuario por email en la base de datos
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));

        // 2. Crear la lista de authorities (roles)
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extraer el nombre del rol desde la entidad RolEntity
        if (usuario.getRol() != null && usuario.getRol().getNombreRol() != null) {
            String nombreRol = usuario.getRol().getNombreRol();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol));
        } else {
            // Si no tiene rol, asignar uno por defecto
            authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }

        // 3. Retornar un objeto UserDetails de Spring Security
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContraseniaHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
