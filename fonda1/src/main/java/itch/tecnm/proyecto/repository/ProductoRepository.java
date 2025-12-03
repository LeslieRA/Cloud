package itch.tecnm.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import feign.Param;
import itch.tecnm.proyecto.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	 List<Producto> findByEstadoTrue();
	 
	 @Query("SELECT p FROM Producto p WHERE p.estado = true " +
	           "AND (:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
	           "AND (:idTipo IS NULL OR p.tipo.id_tipo = :idTipo) " +
	           "AND (:precioMin IS NULL OR p.precioProducto >= :precioMin) " +
	           "AND (:precioMax IS NULL OR p.precioProducto <= :precioMax)")
	 
	 
	    List<Producto> buscarProductosActivos(
	            @Param("nombre") String nombre,
	            @Param("idTipo") Integer idTipo,
	            @Param("precioMin") Double precioMin,
	            @Param("precioMax") Double precioMax
	    );

}
