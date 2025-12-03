package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.TipoDto;
import itch.tecnm.proyecto.entity.Tipo;

public class TipoMapper {
	public static TipoDto mapToTipoDto(Tipo tipo) {
        return new TipoDto(
                tipo.getId_tipo(),
                tipo.getTipo(),
                tipo.getDescripcion()
        );
    }

    public static Tipo mapToTipo(TipoDto tipoDto) {
        return new Tipo(
                tipoDto.getId_tipo(),
                tipoDto.getTipo(),
                tipoDto.getDescripcion()
        );
    }
}
