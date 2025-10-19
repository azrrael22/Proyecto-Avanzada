package uniquindio.edu.co.Proyecto_Avanzada.configuracion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Indica que esta clase manejará excepciones de forma global
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de validación lanzadas por @Valid.
     * Devuelve un 400 Bad Request con mensajes de error específicos por cada campo.
     * @param ex La excepción que se lanza cuando falla la validación.
     * @return Un ResponseEntity que contiene los mensajes de error estructurados.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // Especifica que este método maneja MethodArgumentNotValidException
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Asegura que la respuesta siempre sea 400
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Recorremos todos los errores de validación de campos
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // Obtenemos el nombre del campo que falló
            String errorMessage = error.getDefaultMessage(); // Obtenemos el mensaje de la validación (ej: "La contraseña debe...")
            errors.put(fieldName, errorMessage); // Agregamos el campo y su error al mapa
        });

        // Estructuramos el cuerpo de la respuesta final
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Falló la validación");
        responseBody.put("detalles", errors); // Incluimos el mapa con los errores específicos

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }


}