package com.olinnova.mentordoctor.dto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCreator {

    public void createFile(String filePath) {
        Path path = Paths.get(filePath);
        Path parentDirectory = path.getParent();

        try {
            // Validar si la ruta del archivo es válida
            if (Files.exists(path)) {
                System.out.println("El archivo ya existe en la ruta: " + path);
                Files.delete(path); // Borrar el archivo existente
                System.out.println("Se borró el archivo existente.");
            }

            // Validar si la ruta del directorio padre existe
            if (!Files.exists(parentDirectory)) {
                // Crear el directorio padre si no existe
                Files.createDirectories(parentDirectory);
                System.out.println("Se creó el directorio: " + parentDirectory);
            }

            // Crear el archivo
            Files.createFile(path);
            System.out.println("Se creó el archivo correctamente: " + path);
        } catch (IOException e) {
            System.err.println("Error al crear o borrar el archivo: " + e.getMessage());
        }
    }
}