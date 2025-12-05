package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.EmpleadoDto;
import itch.tecnm.proyecto.entity.Empleado;
import itch.tecnm.proyecto.entity.Empleado.Puesto; // Importa el Enum

public class EmpleadoMapper {

    /**
     * Convierte un String al Enum Puesto.
     */
    public static Puesto toPuesto(String puestoStr) {
        if (puestoStr == null || puestoStr.trim().isEmpty()) {
             // Es mejor lanzar una excepción si el puesto es nulo, ya que la BD lo requiere
             throw new IllegalArgumentException("El puesto no puede estar vacío");
        }
        // Convierte el string a mayúsculas para coincidir con el Enum
        return Puesto.valueOf(puestoStr.trim().toUpperCase());
    }

    /**
     * Convierte una Entidad Empleado a un DTO EmpleadoDto (para enviar a React).
     * Se añade el campo de seguridad (username).
     */
    public static EmpleadoDto mapToEmpleadoDto(Empleado empleado) {
        if (empleado == null) return null;
        
        return new EmpleadoDto(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                // Convierte el Enum a String
                empleado.getPuesto() != null ? empleado.getPuesto().name() : null,
                empleado.getUsername(),
                empleado.getPassword()// <-- CAMPO AÑADIDO
        );
    }

    /**
     * Convierte un DTO EmpleadoDto a una Entidad Empleado.
     * Usa setters para evitar problemas con la contraseña, que NO está en el DTO.
     */
    public static Empleado mapToEmpleado(EmpleadoDto empleadoDto) {
        if (empleadoDto == null) return null;

        // Usa el constructor sin argumentos y luego los setters
        Empleado empleado = new Empleado(); 
        
        empleado.setIdEmpleado(empleadoDto.getIdEmpleado());
        empleado.setNombre(empleadoDto.getNombre());
        empleado.setPuesto(toPuesto(empleadoDto.getPuesto()));
        empleado.setUsername(empleadoDto.getUsername());
        empleado.setPassword(empleadoDto.getPassword());// <-- CAMPO AÑADIDO
        
        // La contraseña se omite INTENCIONALMENTE.
        // Solo debe ser manejada y encriptada por el AuthService.
        return empleado;
    }
}