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
public class MesaDto {
	private Integer idMesa;
    private Integer numero;
    private Integer capacidad;
    private String  ubicacion;
    private boolean estado; // --- 👈 NUEVO CAMPO AÑADIDO ---
}
