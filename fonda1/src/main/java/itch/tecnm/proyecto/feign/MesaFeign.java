package itch.tecnm.proyecto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.tecnm.proyecto.dto.MesaDto; // Asegúrate de tener el DTO placeholder en fonda1

@FeignClient(name = "reservaciones-service-mesa", url = "http://localhost:7076/api/mesa")
public interface MesaFeign {
	@GetMapping("/{id}")
    ResponseEntity<MesaDto> getMesaById(@PathVariable("id") Integer mesaId); // 2. Cambia
}