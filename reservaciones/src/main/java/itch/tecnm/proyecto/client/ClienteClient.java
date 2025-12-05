package itch.tecnm.proyecto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam; // Importante

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// 1. DESCOMENTA Y CONFIGURA
@FeignClient(name = "restaurante1-service", url = "http://localhost:7072/api/cliente")
public interface ClienteClient {

    @GetMapping("/{id}")
    ClienteDto findById(@PathVariable("id") Integer id);

    // 2. AGREGA ESTE MÉTODO
    @GetMapping("/buscar-por-correo")
    ClienteDto findByCorreo(@RequestParam("correo") String correo);

    @JsonIgnoreProperties(ignoreUnknown = true)
    class ClienteDto {
        @JsonAlias({"id_cliente", "idCliente"})
        private Integer idCliente;
        private String nombreCliente;
        private String correoCliente;
        private String telefonoCliente;
        
        // 3. AGREGA EL PASSWORD
        private String password;

        // Getters y Setters
        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
        
        public String getNombreCliente() { return nombreCliente; }
        public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
        
        public String getCorreoCliente() { return correoCliente; }
        public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
        
        public String getTelefonoCliente() { return telefonoCliente; }
        public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}