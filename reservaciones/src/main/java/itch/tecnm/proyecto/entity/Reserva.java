package itch.tecnm.proyecto.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name="reserva")
public class Reserva {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreserva")
    private Integer idReserva;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;    

    // FK local a mesa
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idmesa",
            foreignKey = @ForeignKey(name = "fk_reserva_mesa"))
    private Mesa mesa;

    // Cliente de MS Restaurante -> sólo guardamos el ID
    @Column(name = "idcliente")
    private Integer idCliente;
    
    @Enumerated(EnumType.STRING) // Guarda el nombre del estado (ej. "PENDIENTE") en la BD
    @Column(name="estado")
    private EstadoReserva estado;
    
    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoReserva.PENDIENTE;
        }
    }
}
