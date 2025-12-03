package itch.tecnm.proyecto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.tecnm.proyecto.dto.ReservaDto;

@FeignClient(name = "reservaciones-service-reserva", url = "http://localhost:7076/api/reserva")
public interface ReservaFeign {
	@GetMapping("/{id}")
    ResponseEntity<ReservaDto> getReservaById(@PathVariable("id") Integer reservaId); // 2. Cambia
}