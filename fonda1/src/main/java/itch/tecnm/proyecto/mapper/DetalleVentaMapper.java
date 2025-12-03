package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.DetalleVentaDto;
import itch.tecnm.proyecto.entity.DetalleVenta;
import itch.tecnm.proyecto.entity.Producto;

public class DetalleVentaMapper {
	
    public static DetalleVentaDto mapToDetalleVentaDto(DetalleVenta dv) {
        if (dv == null) return null;

        // Obtenemos el ID del producto de forma segura
        Integer idProducto = (dv.getProducto() != null)
                ? dv.getProducto().getId_producto()
                : null;
        
        // --- 👇 LÓGICA AÑADIDA 👇 ---
        // Obtenemos el nombre del producto, también de forma segura
        String nombreProducto = (dv.getProducto() != null)
                ? dv.getProducto().getNombreProducto()
                : "Producto no encontrado"; // Mensaje por si el producto fue eliminado

        // Pasamos el nuevo campo 'nombreProducto' al constructor del DTO
        return new DetalleVentaDto(
                dv.getIdDetalle(),
                idProducto,
                dv.getCantidad(),
                dv.getPrecioUnitario(),
                nombreProducto 
        );
    }

    public static DetalleVenta mapToDetalleVenta(DetalleVentaDto dto) {
        if (dto == null) return null;

        Producto p = null;
        if (dto.getIdProducto() != null) {
            p = new Producto();
            p.setId_producto(dto.getIdProducto());
            p.setNombreProducto(dto.getNombreProducto());
        }

        return new DetalleVenta(
                dto.getIdDetalle(),
                null,
                p,
                dto.getCantidad(),
                dto.getPrecioUnitario()
        );
    }
}
