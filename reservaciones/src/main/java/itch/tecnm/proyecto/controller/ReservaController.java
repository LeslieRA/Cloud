package itch.tecnm.proyecto.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.ReservaDto;
import itch.tecnm.proyecto.service.ReservaService;
import lombok.AllArgsConstructor;

// Especifica el origen de tu React
@CrossOrigin(origins = "http://localhost:3000") 
@AllArgsConstructor
@RestController
@RequestMapping("/api/reserva")
public class ReservaController {
	private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDto> crearReserva(@RequestBody ReservaDto reservaDto) {
        ReservaDto guardada = reservaService.createReserva(reservaDto);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ReservaDto> getReservaById(@PathVariable("id") Integer reservaId) {
        return ResponseEntity.ok(reservaService.getReservaById(reservaId));
    }

    // --- SE ELIMINÓ EL MÉTODO DUPLICADO ---

    @PutMapping("{id}")
    public ResponseEntity<ReservaDto> updateReserva(@PathVariable("id") Integer reservaId,
                                                    @RequestBody ReservaDto updateReserva) {
        return ResponseEntity.ok(reservaService.updateReserva(reservaId, updateReserva));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteReserva(@PathVariable("id") Integer reservaId) {
        reservaService.deleteReserva(reservaId);
        return ResponseEntity.ok("Reserva eliminada");
    }
    
    // --- 👇 ESTE ES AHORA EL ÚNICO MÉTODO PARA GET /api/reserva 👇 ---
    // Maneja tanto la lista completa (sin filtro) como la filtrada por fecha
    @GetMapping
    public ResponseEntity<List<ReservaDto>> getAllReserva(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        // Llama al método del servicio (que debería llamarse 'getAllReserva')
        List<ReservaDto> reservas = reservaService.getReservaDate(fecha);
        return ResponseEntity.ok(reservas);
    }
}