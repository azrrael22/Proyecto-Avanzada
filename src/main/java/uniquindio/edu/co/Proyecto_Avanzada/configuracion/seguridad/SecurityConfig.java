package uniquindio.edu.co.Proyecto_Avanzada.configuracion.seguridad;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuración de seguridad de Spring Security con JWT y CORS
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Configuración de la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ========== HABILITAR CORS ==========
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // ========== DESHABILITAR CSRF ==========
                .csrf(AbstractHttpConfigurer::disable)

                // ========== CONFIGURACIÓN DE AUTORIZACIÓN ==========
                .authorizeHttpRequests(auth -> auth
                        // ========== PERMITIR TODAS LAS PETICIONES OPTIONS (PREFLIGHT) ==========
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ========== ENDPOINTS PÚBLICOS ==========
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()

                        // ========== ENDPOINTS PROTEGIDOS POR ROL ==========
                        .requestMatchers("/api/usuario/**").hasAnyRole("USUARIO", "ANFITRION", "ADMINISTRADOR")
                        .requestMatchers("/api/anfitrion/**").hasAnyRole("ANFITRION", "ADMINISTRADOR")
                        .requestMatchers("/api/administrador/**").hasRole("ADMINISTRADOR")

                        // ========== CUALQUIER OTRA PETICIÓN REQUIERE AUTENTICACIÓN ==========
                        .anyRequest().authenticated()
                )

                // ========== GESTIÓN DE SESIONES (STATELESS PARA JWT) ==========
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ========== PROVEEDOR DE AUTENTICACIÓN ==========
                .authenticationProvider(authenticationProvider())

                // ========== FILTRO JWT ==========
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Proveedor de autenticación con UserDetailsService y PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Gestor de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
