package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.ReservaDto;
import itch.tecnm.proyecto.entity.Mesa;
import itch.tecnm.proyecto.entity.Reserva;
// Asegúrate de que tus DTO y Entidad también tengan el campo 'estado'

public class ReservaMapper {

    // Método para convertir de Entidad a DTO (para enviar a React)
    public static ReservaDto mapToReservaDto(Reserva reserva) {
        return new ReservaDto(
                reserva.getIdReserva(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getMesa() != null ? reserva.getMesa().getIdMesa() : null,
                reserva.getIdCliente(),
                reserva.getEstado() // <-- SE AGREGA EL CAMPO 'estado'
        );
    }

    // Método para convertir de DTO a Entidad (para recibir de React)
    public static Reserva mapToReserva(ReservaDto reservaDto) {
        Mesa mesa = null;
        if (reservaDto.getIdMesa() != null) {
            mesa = new Mesa();
            mesa.setIdMesa(reservaDto.getIdMesa());
        }
        
        return new Reserva(
                reservaDto.getIdReserva(),
                reservaDto.getFecha(),
                reservaDto.getHora(),
                mesa,
                reservaDto.getIdCliente(),
                reservaDto.getEstado() // <-- SE AGREGA EL CAMPO 'estado'
        );
    }
}
