package config; // (Asegúrate que el paquete sea correcto)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // La URL que usará React (ej. /uploads/mi-imagen.jpg)
        registry.addResourceHandler("/uploads/**")
                // La carpeta física en tu servidor donde están las imágenes
                .addResourceLocations("file:./uploads/");
    }
    

}