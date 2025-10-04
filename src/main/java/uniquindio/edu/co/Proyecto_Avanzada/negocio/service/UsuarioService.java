// Ruta: src/main/java/uniquindio/edu/co/Proyecto_Avanzada/negocio/service/UsuarioService.java

package uniquindio.edu.co.Proyecto_Avanzada.negocio.service;

import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioCreateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginRequestDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Autenticacion.LoginResponseDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Usuario.UsuarioUpdateDTO;
import uniquindio.edu.co.Proyecto_Avanzada.negocio.dto.dtos_Password.CambiarPasswordDTO;

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

    /**
     * Actualiza el perfil de un usuario existente.
     * @param usuarioId ID del usuario a modificar.
     * @param updateDTO DTO con la nueva información.
     * @return El DTO del usuario con los datos actualizados.
     * @throws Exception Si el usuario no se encuentra.
     */
    UsuarioDTO actualizarPerfil(Long usuarioId, UsuarioUpdateDTO updateDTO) throws Exception;

    /**
     * Cambia la contraseña de un usuario validando su contraseña actual.
     * @param usuarioId ID del usuario que cambia la contraseña.
     * @param passwordDTO DTO con la contraseña actual, la nueva y su confirmación.
     * @throws Exception Si la contraseña actual es incorrecta o las nuevas no coinciden.
     */
    void cambiarPassword(Long usuarioId, CambiarPasswordDTO passwordDTO) throws Exception;

}