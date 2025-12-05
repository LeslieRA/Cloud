package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.AtenderDto;
import itch.tecnm.proyecto.entity.Atender;
import itch.tecnm.proyecto.entity.Empleado;

public class AtenderMapper {
	public static AtenderDto mapToAtenderDto(Atender atender) {
        return new AtenderDto(
                atender.getIdAtender(),
                atender.getEmpleado() != null ? atender.getEmpleado().getIdEmpleado() : null,
                atender.getIdVenta()
        );
    }

    public static Atender mapToAtender(AtenderDto atenderDto) {
        Empleado empleado = null;
        if (atenderDto.getIdEmpleado() != null) {
            empleado = new Empleado();
            empleado.setIdEmpleado(atenderDto.getIdEmpleado()); // referencia por id
        }
        return new Atender(
                atenderDto.getIdAtender(),
                empleado,
                atenderDto.getIdVenta()
        );
    }
}
