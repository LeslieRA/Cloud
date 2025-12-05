package itch.tecnm.proyecto.controller;

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

import itch.tecnm.proyecto.dto.AtenderDto;
import itch.tecnm.proyecto.service.AtenderService;
import lombok.AllArgsConstructor;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/atender")
public class AtenderController {
	private final AtenderService atenderService;

    @PostMapping
    public ResponseEntity<AtenderDto> crearAtender(@RequestBody AtenderDto atenderDto) {
        AtenderDto guardada = atenderService.createAtender(atenderDto);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AtenderDto> getAtenderById(@PathVariable("id") Integer atenderId) {
        return ResponseEntity.ok(atenderService.getAtenderById(atenderId));
    }

    @GetMapping
    public ResponseEntity<List<AtenderDto>> getAllAtender() {
        return ResponseEntity.ok(atenderService.getAllAtender());
    }

    @PutMapping("{id}")
    public ResponseEntity<AtenderDto> updateAtender(@PathVariable("id") Integer atenderId,
                                                    @RequestBody AtenderDto updateAtender) {
        return ResponseEntity.ok(atenderService.updateAtender(atenderId, updateAtender));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAtender(@PathVariable("id") Integer atenderId) {
        atenderService.deleteAtender(atenderId);
        return ResponseEntity.ok("Atención eliminada");
    }
}
