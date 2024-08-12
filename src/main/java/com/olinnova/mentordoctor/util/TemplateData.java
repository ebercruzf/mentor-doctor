package com.olinnova.mentordoctor.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TemplateData {

    // Implementa esta clase para manejar los datos de reemplazo de forma segura
    private final Map<String, String> dataMap;

    public TemplateData() {
        this.dataMap = new HashMap<>();
    }

    public void addValue(String key, String value) {
        if (key != null && value != null) {
            String sanitizedValue = sanitizeInput(value);
            dataMap.put(key, sanitizedValue);
        }
    }

    public String getValue(String key) {
        return dataMap.getOrDefault(key, "");
    }

    private String sanitizeInput(String input) {
        // Implementa aquí la lógica de sanitización
        // Por ejemplo, puedes eliminar o escapar caracteres especiales
        if (input == null) {
            return "";
        }

        // Paso 1: Eliminar caracteres de control y otros no imprimibles
        input = input.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // Paso 2: Limitar la longitud de la entrada
        input = input.substring(0, Math.min(input.length(), 1000));

        // Paso 3: Eliminar etiquetas HTML/XML
        input = input.replaceAll("<[^>]*>", "");

        // Paso 4: Prevenir inyección de JavaScript
        input = input.replaceAll("(?i)script", "scr!pt");

        // Paso 5: Eliminar caracteres potencialmente peligrosos
        input = input.replaceAll("[\\\\\"'`]", "");

        // Paso 6: Desescapar entidades HTML
        input = StringEscapeUtils.unescapeHtml4(input);

        // Paso 7: Validar y limpiar caracteres no permitidos
        input = input.replaceAll("[^A-Za-z0-9.,;:!?()\\s\\-_áéíóúÁÉÍÓÚñÑ]", "");



        return input;
    }
}
