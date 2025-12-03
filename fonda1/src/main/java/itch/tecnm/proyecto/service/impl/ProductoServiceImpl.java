package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.dto.ProductoDto;
import itch.tecnm.proyecto.entity.Producto;
import itch.tecnm.proyecto.mapper.ProductoMapper;
import itch.tecnm.proyecto.mapper.TipoMapper;
import itch.tecnm.proyecto.repository.ProductoRepository;
import itch.tecnm.proyecto.service.ProductoService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    
    private ProductoRepository productoRepository;
	
	@Override
	public ProductoDto createProducto(ProductoDto productoDto) {
		Producto producto = ProductoMapper.mapToProducto(productoDto);
		Producto savedProducto = productoRepository.save(producto);
		return ProductoMapper.mapToProductoDto(savedProducto);
	}

	@Override
	public ProductoDto getProductoById(Integer productoId) {
		Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
		return ProductoMapper.mapToProductoDto(producto);
	}

	@Override
	public List<ProductoDto> getAllProductos() {
		// ✅ CORRECTO: Llama al método del repositorio que solo trae productos activos
		List<Producto> productos = productoRepository.findByEstadoTrue();
		
		return productos.stream().map(ProductoMapper::mapToProductoDto)
				.collect(Collectors.toList());
	}

	@Override
	public ProductoDto updateProducto(Integer productoId, ProductoDto updateProducto) {
		Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
		
		producto.setNombreProducto(updateProducto.getNombreProducto());
		producto.setDescripcionProducto(updateProducto.getDescripcionProducto());
		producto.setPrecioProducto(updateProducto.getPrecioProducto());
		producto.setTipo(TipoMapper.mapToTipo(updateProducto.getTipo()));
		producto.setEstado(updateProducto.isEstado());
		producto.setImagenRuta(updateProducto.getImagenRuta());
		
		Producto updateProductoObj = productoRepository.save(producto);
		return ProductoMapper.mapToProductoDto(updateProductoObj);
	}

	@Override
	public void deleteProducto(Integer productoId) {
		// ✅ CORRECTO: Implementa el "Soft Delete"
		Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
		
		// En lugar de borrar, lo marca como inactivo
		producto.setEstado(false);
		
		productoRepository.save(producto);
	}

	@Override
	public List<ProductoDto> getAllProductosForAdmin() {
		// Este método usa findAll() para traer TODOS los productos
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(ProductoMapper::mapToProductoDto)
                .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductoDto> buscarProductos(String nombre, Integer idTipo, Double precioMin, Double precioMax) {
		// Maneja el caso de "Infinity" que puede venir de React
        Double precioMaxFinal = (precioMax != null && Double.isInfinite(precioMax)) ? null : precioMax;

        List<Producto> productos = productoRepository.buscarProductosActivos(
            nombre, 
            idTipo, 
            precioMin, 
            precioMaxFinal
        );
        
        return productos.stream()
                .map(ProductoMapper::mapToProductoDto)
                .collect(Collectors.toList());
	}
}
