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

import itch.tecnm.proyecto.dto.TipoDto;
import itch.tecnm.proyecto.service.TipoService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/tipoProducto")
public class TipoController {
	//Inyección de dependencia
			private TipoService tipoService;
			
			//Construir el REST API para agregar un tipo
			@PostMapping
			public ResponseEntity<TipoDto> crearTipo(@RequestBody TipoDto tipoDto){
				TipoDto guardarTipo = tipoService.createTipo(tipoDto);
				//Respose entity permite utilicar información a traves de http
				return new ResponseEntity<>(guardarTipo, HttpStatus.CREATED);
			}
			
			//Construir el get del tipo REST API
					@GetMapping("{id}")
					public ResponseEntity<TipoDto> getTipoById(@PathVariable("id") Integer tipoId){
						
						TipoDto tipoDto = tipoService.getTipoById(tipoId);
						return ResponseEntity.ok(tipoDto);
					}

				//REST API. Construir el get para todos los tipos
				@GetMapping 
				public ResponseEntity<List<TipoDto>> getAllTipo(){
					List<TipoDto> tipos = tipoService.getAllTipo();
					return ResponseEntity.ok(tipos);
				}
				
				// Construir REST API Update Tipo.
					// Exponer un endpoint HTTP PUT para actualizar un Tipo
					@PutMapping("{id}")
					public ResponseEntity<TipoDto> updateTipo(@PathVariable("id") Integer id,
							@RequestBody TipoDto updateTipo) {
						TipoDto tipoDto = tipoService.updateTipo(id, updateTipo);
						return ResponseEntity.ok(tipoDto);
					}
				
				//Construir delete Tipo REST API
					@DeleteMapping("{id}")
					public ResponseEntity<String> deleteTipo(@PathVariable("id") Integer tipoId){
						tipoService.deleteTipo(tipoId);
						return ResponseEntity.ok("Registro  de tipo eliminado");
					}
}
