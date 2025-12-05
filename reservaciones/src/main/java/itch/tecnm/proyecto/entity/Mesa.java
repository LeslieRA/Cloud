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
@Table(name="mesa")
public class Mesa {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmesa")
    private Integer idMesa;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "ubicacion")
    private String ubicacion;
    
 // false = Desocupada, true = Ocupada
    @Column(name="estado")
    private boolean estado = true;
}
