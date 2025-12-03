package itch.tecnm.proyecto.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import itch.tecnm.proyecto.dto.VentaDto;
import itch.tecnm.proyecto.service.VentaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/venta")

public class VentaController {
	private final VentaService ventaService;

    // Crear venta (con sus detalles)
    @PostMapping
    public ResponseEntity<VentaDto> crearVenta(@RequestBody VentaDto ventaDto) {
        VentaDto guardada = ventaService.createVenta(ventaDto);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    // Obtener venta por id
    @GetMapping("{id}")
    public ResponseEntity<VentaDto> getVentaById(@PathVariable("id") Integer ventaId) {
        VentaDto venta = ventaService.getVentaById(ventaId);
        if (venta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(venta);
    }

    // Listar todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDto>> getAllVentas() {
        List<VentaDto> ventas = ventaService.getAllVentas();
        return ResponseEntity.ok(ventas);
    }

    // Actualizar venta (reemplaza/ajusta detalles)
    @PutMapping("{id}")
    public ResponseEntity<VentaDto> updateVenta(@PathVariable("id") Integer ventaId,
                                                @RequestBody VentaDto updateVenta) {
        VentaDto venta = ventaService.updateVenta(ventaId, updateVenta);
        return ResponseEntity.ok(venta);
    }

    // Eliminar venta
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVenta(@PathVariable("id") Integer ventaId) {
        ventaService.deleteVenta(ventaId);
        return ResponseEntity.ok("Venta eliminada");
    }
    
 // --- 👇 AÑADE ESTE ENDPOINT PARA EL TICKET 👇 ---
    @GetMapping("/{idVenta}/ticket")
    public void getTicketPdf(@PathVariable("idVenta") Integer idVenta, HttpServletResponse response) {
        try {
            ventaService.generarTicketPdf(idVenta, response);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            // Opcional: manejar el error de forma más elegante
        }
    }
}
