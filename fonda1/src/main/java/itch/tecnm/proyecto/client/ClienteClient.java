package itch.tecnm.proyecto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Debe coincidir con spring.application.name=restaurante (en minúsculas)
//@FeignClient(name = "restaurante1", path = "/api/cliente")
public interface ClienteClient {
	@GetMapping("/{id}")
    ClienteDto findById(@PathVariable("id") Integer id);

    @JsonIgnoreProperties(ignoreUnknown = true)
   class ClienteDto {
        @JsonAlias({"id_cliente", "idCliente"})
     private Integer idCliente;
        private String nombreCliente;
       private String correoCliente;
        private String telefonoCliente;

        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
        public String getNombreCliente() { return nombreCliente; }
        public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
       public String getCorreoCliente() { return correoCliente; }
       public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
       public String getTelefonoCliente() { return telefonoCliente; }
        public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    }
}
