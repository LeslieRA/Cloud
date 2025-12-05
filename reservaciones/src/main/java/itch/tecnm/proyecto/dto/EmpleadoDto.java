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
public class EmpleadoDto {
	private Integer idEmpleado;
    private String  nombre;
    private String  puesto;
    private String  username;
    private String password;// Campo de seguridad (público)
}
