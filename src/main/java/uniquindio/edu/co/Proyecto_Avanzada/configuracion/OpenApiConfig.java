package uniquindio.edu.co.Proyecto_Avanzada.configuracion;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistema de Gestión de Alojamientos API",
                version = "1.0.0",
                description = "API REST para la gestión completa de alojamientos, reservas y comentarios. " +
                        "Permite a usuarios buscar y reservar alojamientos, a anfitriones gestionar sus propiedades, " +
                        "y a administradores supervisar el sistema.",
                contact = @Contact(
                        name = "Equipo de Desarrollo",
                        email = "dev@alojamientos.com",
                        url = "https://github.com/tu-usuario/Proyecto-Avanzada"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desarrollo"
                ),
                @Server(
                        url = "https://api-test.alojamientos.com",
                        description = "Servidor de Pruebas"
                ),
                @Server(
                        url = "https://api.alojamientos.com",
                        description = "Servidor de Producción"
                )
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa tu token JWT para autenticarte")
                        )
                );
    }
}
