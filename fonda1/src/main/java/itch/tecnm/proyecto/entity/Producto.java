package itch.tecnm.proyecto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok realiza automáticamente los constructores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="producto")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_producto;
	
	@Column(name="nombreProducto")
	private String nombreProducto;
	
	@Column(name="descripcionProducto")
	private String descripcionProducto;
	
	@Column(name="precioProducto")
	private double precioProducto;
	// --- 👇 NUEVO CAMPO AÑADIDO 👇 ---
    @Column(name="estado")
    private boolean estado = true; // Por defecto, los productos son activos
	
	// Relación con Tipo
    @ManyToOne
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    private Tipo tipo;
    
    @Column(name="imagen_ruta")
    private String imagenRuta;
    
 
}
