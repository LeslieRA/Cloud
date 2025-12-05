package itch.tecnm.proyecto.service;

import java.util.List;

import itch.tecnm.proyecto.dto.EmpleadoDto;

public interface EmpleadoService {
	//Agregar un empleado
	EmpleadoDto createEmpleado(EmpleadoDto empeladoDto);
	
	//Buscar un empleado por id
	EmpleadoDto getEmpleadoById(Integer empleadoId);
	
	//Obtener todos los datos de los empleados
	List<EmpleadoDto> getAllEmpleados();
	
	//Construir el REST API para modificar
	EmpleadoDto updateEmpleado(Integer empleadoId, EmpleadoDto updateEmpleado);
	
	//Construir el DELETE REST API de Empleado
	void deleteEmpleado(Integer empledoId);
	
	List<EmpleadoDto> getAllEmpleados(String nombre, String puesto); // Acepta filtros
	
}
