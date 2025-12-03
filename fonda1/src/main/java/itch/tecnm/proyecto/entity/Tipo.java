package itch.tecnm.proyecto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="tipo")
public class Tipo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_tipo;
	
	@Column(name="tipo")
    private String tipo;

	@Column(name="descripcion")
    private String descripcion;
}
