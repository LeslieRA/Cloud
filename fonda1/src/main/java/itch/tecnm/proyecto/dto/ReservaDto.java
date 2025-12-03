package itch.tecnm.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok para generar getters, setters y constructores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDto {
	private Integer   idReserva;
    // @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    // @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    private Integer idMesa;     // FK lógica hacia Mesa
    private Integer idCliente;  // ID de Restaurante (otro MS)
    
   
}
