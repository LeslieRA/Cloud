package itch.tecnm.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import feign.Param;
import itch.tecnm.proyecto.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

    // --- 👇 AÑADE ESTE NUEVO MÉTODO DE BÚSQUEDA 👇 ---
    @Query("SELECT e FROM Empleado e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:puesto IS NULL OR e.puesto = :puesto)")
    List<Empleado> buscarEmpleados(
        @Param("nombre") String nombre,
        @Param("puesto") Empleado.Puesto puesto // Acepta el tipo Enum
    );
    
    
    Optional<Empleado> findByUsername(String username);
}
