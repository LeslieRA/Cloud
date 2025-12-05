package itch.tecnm.proyecto.service;

import java.util.List;

import itch.tecnm.proyecto.dto.MesaDto;

public interface MesaService {
		//Agregar una mesa
		MesaDto createMesa(MesaDto mesaDto);
		
		//Buscar una mesa por id
		MesaDto getMesaById(Integer mesaId);
		
		//Obtener todos los datos de las mesas
		List<MesaDto> getAllMesa();
		
		//Construir el REST API para modificar
		MesaDto updateMesa(Integer mesaId, MesaDto updateMesa);
		
		//Construir el DELETE REST API de Mesa
		void deleteMesa(Integer mesaId);
		
		//CAMBIER EL ESTADO 
		MesaDto toggleEstadoMesa(Integer idMesa);
		
		List<MesaDto> getMesasPorEstado(boolean estado);

}
