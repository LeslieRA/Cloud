package itch.tecnm.proyecto.service;

import java.util.List;

import itch.tecnm.proyecto.dto.TipoDto;

public interface TipoService {
	//Agregar un tipo
		TipoDto createTipo(TipoDto tipoDto);
		
		//Buscar un tipo por id
		TipoDto getTipoById(Integer tipoId);
		
		//Obtener todos los datos de los tipos
		List<TipoDto> getAllTipo();
		
		//Construir el REST API para modificar
		TipoDto updateTipo(Integer tipoId, TipoDto updateTipo);
		
		//Construir el DELETE REST API de tipo
		void deleteTipo(Integer tipoId);
}
