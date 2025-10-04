// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/UsuarioService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;

public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuarioCreateDTO Datos para crear el usuario.
     * @return El DTO del usuario recién creado.
     * @throws Exception si el email ya existe.
     */
    UsuarioDTO registrarUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception;

}