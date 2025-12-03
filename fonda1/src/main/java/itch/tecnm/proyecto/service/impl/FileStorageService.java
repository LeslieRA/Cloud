package itch.tecnm.proyecto.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    // Define la ruta donde se guardarán las imágenes
    // Puedes poner esto en tu 'application.properties'
    private final Path rootLocation = Paths.get("uploads");

    // Un método para inicializar la carpeta (puedes llamarlo al inicio)
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar la carpeta de uploads", e);
        }
    }

    // El método principal para guardar el archivo
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Error: El archivo está vacío.");
            }

            // Asegurarse de que el nombre del archivo sea seguro
            String filename = file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();

            // Copia el archivo al directorio de destino
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Devuelve el nombre del archivo para guardarlo en la BD
            return filename; 

        } catch (IOException e) {
            throw new RuntimeException("Falló al guardar el archivo.", e);
        }
    }
}