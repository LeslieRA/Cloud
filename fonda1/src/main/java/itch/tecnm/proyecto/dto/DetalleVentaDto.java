package itch.tecnm.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok para generar getters, setters y constructores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDto {
	private Integer idDetalle;
    private Integer idProducto;
    private Integer cantidad;
    private Double precioUnitario;

 // --- 👇 CAMPO AÑADIDO 👇 ---
    // Este campo se usará para mostrar el nombre, no para recibirlo
    @JsonProperty(access = Access.READ_ONLY)
    private String nombreProducto;
}
