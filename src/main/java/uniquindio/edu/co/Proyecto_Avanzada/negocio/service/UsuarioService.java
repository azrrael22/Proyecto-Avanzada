// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/UsuarioService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginRequestDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginResponseDTO;

public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuarioCreateDTO Datos para crear el usuario.
     * @return El DTO del usuario recién creado.
     * @throws Exception si el email ya existe.
     */
    UsuarioDTO registrarUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception;

    /**
     * Autentica un usuario en el sistema.
     * @param loginRequestDTO Datos para el login (email y password).
     * @return El DTO con el token JWT y los datos del usuario.
     * @throws Exception si las credenciales son incorrectas o el usuario no existe.
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception;

}