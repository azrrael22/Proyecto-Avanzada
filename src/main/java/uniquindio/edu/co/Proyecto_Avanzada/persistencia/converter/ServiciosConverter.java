package uniquindio.edu.co.Proyecto_Avanzada.persistencia.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter para convertir List<String> a JSON y viceversa
 * para el campo servicios en Alojamiento
 */
@Converter
@Slf4j
public class ServiciosConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> servicios) {
        if (servicios == null || servicios.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(servicios);
        } catch (JsonProcessingException e) {
            log.error("Error al convertir servicios a JSON: {}", e.getMessage());
            return "[]";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String serviciosJson) {
        if (serviciosJson == null || serviciosJson.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(serviciosJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error al convertir JSON a lista de servicios: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}