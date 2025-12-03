package itch.tecnm.proyecto.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import itch.tecnm.proyecto.service.impl.FileStorageService;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React
public class FileUploadController {

    private final FileStorageService storageService;

    @Autowired
    public FileUploadController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "C:/uploads/"; // Ruta donde se guardan las imágenes
            Path path = Paths.get(uploadDir + file.getOriginalFilename());

            // Crear carpeta si no existe
            Files.createDirectories(path.getParent());

            // Guardar archivo
            file.transferTo(path.toFile());

            return ResponseEntity.ok("Archivo subido correctamente: " + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al subir el archivo: " + e.getMessage());
        }
    }

    // 👇 NUEVO MÉTODO PARA MOSTRAR LAS IMÁGENES
    @GetMapping("/{filename:.+}")
    public ResponseEntity<byte[]> verArchivo(@PathVariable String filename) {
        try {
            String uploadDir = "C:/uploads/"; // misma ruta donde guardas las imágenes
            Path path = Paths.get(uploadDir + filename);

            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            // Detectar tipo MIME (image/jpeg, image/png, etc.)
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(data);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}