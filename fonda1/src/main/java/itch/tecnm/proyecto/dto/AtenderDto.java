package itch.tecnm.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok para generar getters, setters y constructores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtenderDto {
	private Integer idAtender;
    private Integer idEmpleado;  // FK lógica hacia Empleado
    private Integer idVenta;     // ID de Fonda (otro MS)
}
