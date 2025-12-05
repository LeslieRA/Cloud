package itch.tecnm.proyecto.service;

import java.util.List;

import itch.tecnm.proyecto.dto.AtenderDto;

public interface AtenderService {
	//Agregar una atencion
	AtenderDto createAtender(AtenderDto atenderDto);
			
	//Buscar una atender por id
	AtenderDto getAtenderById(Integer atenderId);
			
	//Obtener todos los datos de las atenciones
	List<AtenderDto> getAllAtender();
			
	//Construir el REST API para modificar
	AtenderDto updateAtender(Integer atenderId, AtenderDto updateAtender);
			
	//Construir el DELETE REST API de Atender
	void deleteAtender(Integer atenderId);
}
