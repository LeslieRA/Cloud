package itch.tecnm.proyecto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
@Table(name="atender")
public class Atender {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idatender")
    private Integer idAtender;

    // Empleado es local -> FK real
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idempleado", nullable = false,
            foreignKey = @ForeignKey(name = "fk_atender_empleado"))
    private Empleado empleado;

    // Venta de MS Fonda -> sólo guardamos el ID
    @Column(name = "idventa", nullable = false)
    private Integer idVenta;
}
