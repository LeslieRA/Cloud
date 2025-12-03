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
public class ProductoDto {
	private int id_producto;
	private String nombreProducto;
	private String descripcionProducto;
	private double precioProducto;
    private TipoDto tipo;
    private boolean estado;
    private String imagenRuta;// --- 👈 NUEVO CAMPO AÑADIDO ---
}
