package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.MesaDto;
import itch.tecnm.proyecto.entity.Mesa;

public class MesaMapper {

    // Convierte una Entidad (de la base de datos) a un DTO (para enviar a React)
    public static MesaDto mapToMesaDto(Mesa mesa) {
        if (mesa == null) return null;
        
        return new MesaDto(
                mesa.getIdMesa(),
                mesa.getNumero(),
                mesa.getCapacidad(),
                mesa.getUbicacion(),
                mesa.isEstado() // Para boolean, Lombok usa "isEstado()" como getter
        );
    }

    // Convierte un DTO (recibido de React) a una Entidad (para guardar en la BD)
    public static Mesa mapToMesa(MesaDto mesaDto) {
        if (mesaDto == null) return null;

        return new Mesa(
                mesaDto.getIdMesa(),
                mesaDto.getNumero(),
                mesaDto.getCapacidad(),
                mesaDto.getUbicacion(),
                mesaDto.isEstado()
        );
    }
}