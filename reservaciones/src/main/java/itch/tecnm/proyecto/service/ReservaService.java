package itch.tecnm.proyecto.service;

import java.time.LocalDate;
import java.util.List;

import itch.tecnm.proyecto.dto.ReservaDto;

public interface ReservaService {
	//Agregar un reserva
	ReservaDto createReserva(ReservaDto reservaDto);
			
	//Buscar una reserva por id
	ReservaDto getReservaById(Integer reservaId);
			
	//Obtener todos los datos de las reservas
	List<ReservaDto> getAllReserva();
			
	//Construir el REST API para modificar
	ReservaDto updateReserva(Integer reservaId, ReservaDto updateReserva);
			
	//Construir el DELETE REST API de Reserva
	void deleteReserva(Integer reservaId);
	
	List<ReservaDto> getReservaDate(LocalDate fecha); // Ahora acepta un filtro de fecha
}
