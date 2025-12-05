package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.dto.EmpleadoDto;
import itch.tecnm.proyecto.entity.Empleado;
import itch.tecnm.proyecto.entity.Empleado.Puesto;
import itch.tecnm.proyecto.mapper.EmpleadoMapper;
import itch.tecnm.proyecto.repository.EmpleadoRepository;
import itch.tecnm.proyecto.service.EmpleadoService;
import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService{
	private final EmpleadoRepository empleadoRepository;

    @Transactional
    @Override
    public EmpleadoDto createEmpleado(EmpleadoDto empleadoDto) {
        Empleado empleado = EmpleadoMapper.mapToEmpleado(empleadoDto);
        
        // --- 👇 LÓGICA PARA LA CONTRASEÑA CON {noop} 👇 ---
        // Validamos si viene password, si no, ponemos uno por defecto
        if (empleadoDto.getPassword() != null && !empleadoDto.getPassword().isEmpty()) {
            // Le pegamos el prefijo para que funcione sin encriptación real
            empleado.setPassword("{noop}" + empleadoDto.getPassword());
        } else {
            // Contraseña por defecto si se les olvida
            empleado.setPassword("{noop}123");
        }
        // --------------------------------------------------

        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return EmpleadoMapper.mapToEmpleadoDto(savedEmpleado);
    }

    @Transactional(readOnly = true)
    @Override
    public EmpleadoDto getEmpleadoById(Integer empleadoId) {
        Empleado emp = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        return EmpleadoMapper.mapToEmpleadoDto(emp);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmpleadoDto> getAllEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EmpleadoDto updateEmpleado(Integer empleadoId, EmpleadoDto updateEmpleado) {
        Empleado emp = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        if (updateEmpleado.getNombre() != null) emp.setNombre(updateEmpleado.getNombre());
        if (updateEmpleado.getPuesto() != null) {
        	emp.setPuesto(EmpleadoMapper.toPuesto(updateEmpleado.getPuesto()));
        }

        Empleado saved = empleadoRepository.save(emp);
        return EmpleadoMapper.mapToEmpleadoDto(saved);
    }

    @Transactional
    @Override
    public void deleteEmpleado(Integer empleadoId) {
        if (!empleadoRepository.existsById(empleadoId)) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }
        empleadoRepository.deleteById(empleadoId);
    }

	@Override
	public List<EmpleadoDto> getAllEmpleados(String nombre, String puesto) {
		// 1. Convierte el String del filtro de puesto al tipo Enum
        Puesto puestoEnum = null;
        if (puesto != null && !puesto.trim().isEmpty()) {
            try {
                // Asegura que el string coincida con el Enum (ej. "MESERO")
                puestoEnum = Puesto.valueOf(puesto.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si el texto no es un puesto válido, no filtra por puesto
                System.err.println("Puesto de filtro no válido: " + puesto);
            }
        }
        
        // 2. Prepara el filtro de nombre
        String nombreFiltro = (nombre != null && !nombre.trim().isEmpty()) ? nombre : null;

        // 3. Llama al nuevo método del repositorio
        List<Empleado> empleados = empleadoRepository.buscarEmpleados(nombreFiltro, puestoEnum);
        
        // 4. Mapea y devuelve
        return empleados.stream()
                .map(EmpleadoMapper::mapToEmpleadoDto)
                .collect(Collectors.toList());
    
	}
}
