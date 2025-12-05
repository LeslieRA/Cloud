package itch.tecnm.proyecto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.tecnm.proyecto.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	List<Reserva> findByFecha(LocalDate fecha);
}
