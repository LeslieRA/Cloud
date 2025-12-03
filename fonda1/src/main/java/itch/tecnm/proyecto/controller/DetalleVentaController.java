package itch.tecnm.proyecto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.DetalleVentaDto;
import itch.tecnm.proyecto.service.DetalleVentaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/detalleventa")
public class DetalleVentaController {
	private final DetalleVentaService detalleVentaService;

    @GetMapping("{id}")
    public ResponseEntity<DetalleVentaDto> getDetalleById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(detalleVentaService.getDetalleById(id));
    }

    @GetMapping
    public ResponseEntity<List<DetalleVentaDto>> getAllDetalles() {
        return ResponseEntity.ok(detalleVentaService.getAllDetallesVenta());
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<DetalleVentaDto>> getDetallesByVenta(@PathVariable("ventaId") Integer ventaId) {
        return ResponseEntity.ok(detalleVentaService.getDetallesByVenta(ventaId));
    }
}
