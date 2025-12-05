package itch.tecnm.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.tecnm.proyecto.entity.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Integer>{
	
	List<Mesa> findByEstado(boolean estado);

}
