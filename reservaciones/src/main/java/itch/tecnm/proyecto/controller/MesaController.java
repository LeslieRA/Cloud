package itch.tecnm.proyecto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.MesaDto;
import itch.tecnm.proyecto.service.MesaService;
import lombok.AllArgsConstructor;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/mesa")
public class MesaController {
	private final MesaService mesaService;

    @PostMapping
    public ResponseEntity<MesaDto> crearMesa(@RequestBody MesaDto mesaDto) {
        MesaDto guardada = mesaService.createMesa(mesaDto);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<MesaDto> getMesaById(@PathVariable("id") Integer mesaId) {
        return ResponseEntity.ok(mesaService.getMesaById(mesaId));
    }

    @GetMapping
    public ResponseEntity<List<MesaDto>> getAllMesa() {
        return ResponseEntity.ok(mesaService.getAllMesa());
    }

    @PutMapping("{id}")
    public ResponseEntity<MesaDto> updateMesa(@PathVariable("id") Integer mesaId,
                                              @RequestBody MesaDto updateMesa) {
        return ResponseEntity.ok(mesaService.updateMesa(mesaId, updateMesa));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMesa(@PathVariable("id") Integer mesaId) {
        mesaService.deleteMesa(mesaId);
        return ResponseEntity.ok("Mesa eliminada");
    }
    @PatchMapping("{id}/toggle-estado")
    public ResponseEntity<MesaDto> toggleEstadoMesa(@PathVariable("id") Integer mesaId) {
        MesaDto mesaActualizada = mesaService.toggleEstadoMesa(mesaId);
        return ResponseEntity.ok(mesaActualizada);
    }
    @GetMapping("/disponibles")
    public ResponseEntity<List<MesaDto>> getMesasDisponibles() {
        // Asumiendo que 1 (true) = Desocupada
        // Ahora esto funciona porque getMesasPorEstado devuelve List<MesaDto>
        return ResponseEntity.ok(mesaService.getMesasPorEstado(true));
    }
}
