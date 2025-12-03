package itch.tecnm.proyecto.mapper;

import itch.tecnm.proyecto.dto.ProductoDto;
import itch.tecnm.proyecto.entity.Producto;

public class ProductoMapper {
	//Conexion entre las entdades y el DTO
	
	public static ProductoDto mapToProductoDto (Producto producto){
		return new ProductoDto(
				producto.getId_producto(),
				producto.getNombreProducto(),
				producto.getDescripcionProducto(),
				producto.getPrecioProducto(),
				TipoMapper.mapToTipoDto(producto.getTipo()),
				producto.isEstado(),
				producto.getImagenRuta()// --- 👇 LÍNEA AÑADIDA ---
				);
	}
	// 👇 MÉTODO CORREGIDO 👇
		public static Producto mapToProducto(ProductoDto productoDto) {
			return new Producto(
					productoDto.getId_producto(),
					productoDto.getNombreProducto(),
					productoDto.getDescripcionProducto(),
					productoDto.getPrecioProducto(),
					productoDto.isEstado(), // <-- estado ahora va primero
					TipoMapper.mapToTipo(productoDto.getTipo()), 
					productoDto.getImagenRuta()// <-- tipo ahora va al final
					);
		}
	}
