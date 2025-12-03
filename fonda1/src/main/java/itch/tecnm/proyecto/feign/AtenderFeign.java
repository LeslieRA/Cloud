package itch.tecnm.proyecto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.tecnm.proyecto.dto.AtenderDto; // El DTO placeholder en fonda1

@FeignClient(name = "reservaciones-service-atender", url = "http://localhost:7076/api/atender")
public interface AtenderFeign {
	@GetMapping("/venta/{idVenta}")
    ResponseEntity<AtenderDto> getAtenderByIdVenta(@PathVariable("idVenta") Integer idVenta); // 2. Cambia
}