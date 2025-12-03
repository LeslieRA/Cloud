package itch.tecnm.proyecto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.tecnm.proyecto.client.ClienteClient.ClienteDto; // El DTO placeholder en fonda1

@FeignClient(name = "restaurante1-service", url = "http://localhost:7072/api/cliente")
public interface ClienteFeign {
	@GetMapping("/{id}")
    ResponseEntity<ClienteDto> getClienteById(@PathVariable("id") Integer idCliente); // 2. Cambia el tipo de retorno
}