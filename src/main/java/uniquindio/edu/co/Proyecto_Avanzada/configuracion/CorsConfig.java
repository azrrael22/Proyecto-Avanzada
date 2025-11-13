package uniquindio.edu.co.Proyecto_Avanzada.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuración de CORS para permitir peticiones desde el frontend Angular
 * IMPORTANTE: Esta configuración se integra con Spring Security
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ========== ORÍGENES PERMITIDOS ==========
        configuration.setAllowedOriginPatterns(Collections.singletonList("*")); // Acepta cualquier origen en desarrollo
        // Para producción, usar orígenes específicos:
        // configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://tu-dominio.com"));

        // ========== MÉTODOS HTTP PERMITIDOS ==========
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // ========== HEADERS PERMITIDOS ==========
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite todos los headers

        // ========== HEADERS EXPUESTOS ==========
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // ========== PERMITIR CREDENCIALES ==========
        configuration.setAllowCredentials(true);

        // ========== TIEMPO DE CACHEO ==========
        configuration.setMaxAge(3600L); // 1 hora

        // ========== APLICAR A TODOS LOS ENDPOINTS ==========
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}