package itch.tecnm.proyecto.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class VentaDto {
	 	private Integer idVenta;
	 	
	 // Solo se envía al cliente (read-only), se ignora si viene en el requests
	    @JsonProperty(access = Access.READ_ONLY)
	    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
	    private LocalDateTime fechaVenta;
	    private Integer idCliente;
	    
	 // NUEVO: asociar (opcional) una reserva del MS Reservaciones
	    private Integer idReserva;
	    
	    @JsonProperty(access = Access.READ_ONLY) 
	    private Double total;
	    private List<DetalleVentaDto> detalles;
}
