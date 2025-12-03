package itch.tecnm.proyecto.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name="venta",
indexes = {
        @Index(name = "idx_venta_idreserva", columnList = "idreserva")
    })
public class Venta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idventa")
    private Integer idVenta;

    @Column(name = "fechaventa", nullable = false)
    private LocalDateTime fechaVenta;

    // Cliente vive en MS Restaurante -> guardamos solo el ID
    @Column(name = "idcliente", nullable = false)
    private Integer idCliente;

 // **Nuevo**: Reserva (de MS Reservaciones) → solo el ID (opcional)
    @Column(name = "idreserva", nullable = true)
    private Integer idReserva;

    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        // si por alguna razón está seteada, igual la normalizamos
        this.fechaVenta = LocalDateTime.now().withSecond(0).withNano(0);
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaVenta = LocalDateTime.now().withSecond(0).withNano(0);
    }
}
