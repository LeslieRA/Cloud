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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.EmpleadoDto;
import itch.tecnm.proyecto.service.EmpleadoService;
import lombok.AllArgsConstructor;


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
	private final EmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<EmpleadoDto> crearEmpleado(@RequestBody EmpleadoDto empleadoDto) {
        EmpleadoDto guardado = empleadoService.createEmpleado(empleadoDto);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmpleadoDto> getEmpleadoById(@PathVariable("id") Integer empleadoId) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(empleadoId));
    }


    @PutMapping("{id}")
    public ResponseEntity<EmpleadoDto> updateEmpleado(@PathVariable("id") Integer empleadoId,
                                                      @RequestBody EmpleadoDto updateEmpleado) {
        return ResponseEntity.ok(empleadoService.updateEmpleado(empleadoId, updateEmpleado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmpleado(@PathVariable("id") Integer empleadoId) {
        empleadoService.deleteEmpleado(empleadoId);
        return ResponseEntity.ok("Empleado eliminado");
    }
    
    @GetMapping
    public ResponseEntity<List<EmpleadoDto>> getAllEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String puesto
    ) {
        List<EmpleadoDto> empleados = empleadoService.getAllEmpleados(nombre, puesto);
        return ResponseEntity.ok(empleados);
    }
}
