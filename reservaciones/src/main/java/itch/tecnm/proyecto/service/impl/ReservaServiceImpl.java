package itch.tecnm.proyecto.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.dto.MesaDto;
import itch.tecnm.proyecto.dto.ReservaDto;
import itch.tecnm.proyecto.entity.EstadoReserva;
import itch.tecnm.proyecto.entity.Mesa;
import itch.tecnm.proyecto.entity.Reserva;
import itch.tecnm.proyecto.mapper.ReservaMapper;
import itch.tecnm.proyecto.repository.MesaRepository;
import itch.tecnm.proyecto.repository.ReservaRepository;
import itch.tecnm.proyecto.service.MesaService;
import itch.tecnm.proyecto.service.ReservaService;
import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class ReservaServiceImpl implements ReservaService{
	private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    //private final ClienteClient clienteClient; // <-- nuevo
    private final MesaService mesaService;

    @Transactional // ¡MUY IMPORTANTE! Asegura que todo se haga o nada
    @Override
    public ReservaDto createReserva(ReservaDto reservaDto) {
        if (reservaDto.getIdMesa() == null)    throw new IllegalArgumentException("idMesa es obligatorio");
        if (reservaDto.getIdCliente() == null) throw new IllegalArgumentException("idCliente es obligatorio");

        // VALIDAR cliente en Restaurante (Comentado por ahora)
        // ...

        // 1. Busca la Mesa y verifica si está disponible
        Mesa mesa = mesaRepository.findById(reservaDto.getIdMesa())
        		.orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con id" + reservaDto.getIdMesa()));
        		
        
        // Asumiendo true (1) = Desocupada, false (0) = Ocupada
        if (!mesa.isEstado()) {
            throw new IllegalStateException("La mesa #" + mesa.getNumero() + " ya está ocupada.");
        }

        // 2. Mapea el DTO a la entidad Reserva y asocia la Mesa
        Reserva reserva = ReservaMapper.mapToReserva(reservaDto);
        reserva.setMesa(mesa);

        // Opcional: Establece el estado PENDIENTE por defecto si no lo maneja @PrePersist
        if (reserva.getEstado() == null) {
             reserva.setEstado(EstadoReserva.PENDIENTE); // Asegúrate de tener el Enum EstadoReserva
        }

        // 3. Guarda la Reserva
        Reserva reservaGuardada = reservaRepository.save(reserva);

        // --- 👇 LÓGICA FALTANTE AÑADIDA 👇 ---
        // 4. Actualiza el estado de la Mesa a "Ocupada"
        try {
            // Crea un DTO con los datos actuales de la mesa pero con estado 'false' (Ocupado)
            MesaDto mesaParaActualizarDto = new MesaDto(
                    mesa.getIdMesa(),
                    mesa.getNumero(),
                    mesa.getCapacidad(),
                    mesa.getUbicacion(),
                    false // false = Ocupado
            );
            // Llama al servicio de Mesa para guardar el cambio
            mesaService.updateMesa(mesa.getIdMesa(), mesaParaActualizarDto);
            
            System.out.println("Mesa ID " + mesa.getIdMesa() + " marcada como Ocupada."); // Log para verificar

        } catch (Exception e) {
            // Si falla la actualización de la mesa, la transacción revertirá el guardado de la reserva.
            // Es buena idea loggear el error específico.
            System.err.println("Error al actualizar el estado de la mesa ID " + mesa.getIdMesa() + ": " + e.getMessage());
            // Puedes relanzar una excepción más específica si lo necesitas
            throw new RuntimeException("Error al actualizar estado de la mesa tras crear reserva.", e);
        }
        // --- 👆 FIN DE LA LÓGICA AÑADIDA 👆 ---

        // 5. Devuelve el DTO de la reserva guardada
        return ReservaMapper.mapToReservaDto(reservaGuardada);
    }

    @Transactional(readOnly = true)
    @Override
    public ReservaDto getReservaById(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        return ReservaMapper.mapToReservaDto(reserva);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservaDto> getAllReserva() {
        return reservaRepository.findAll().stream()
                .map(ReservaMapper::mapToReservaDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReservaDto updateReserva(Integer reservaId, ReservaDto updateReserva) {
        // 1) Cargar la reserva existente
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

//        // 2) Si te mandan un idCliente (posible cambio), validar en MS Restaurante
//        if (updateReserva.getIdCliente() != null &&
//            !updateReserva.getIdCliente().equals(reserva.getIdCliente())) {
//            try {
//                var remoto = clienteClient.findById(updateReserva.getIdCliente());
//                if (remoto == null || remoto.getIdCliente() == null) {
//                    throw new IllegalArgumentException("Cliente no existe: " + updateReserva.getIdCliente());
//                }
//            } catch (FeignException.NotFound nf) {
//                throw new IllegalArgumentException("Cliente no existe: " + updateReserva.getIdCliente());
//            } catch (FeignException fe) {
//                throw new IllegalStateException("Error consultando Restaurante: " + fe.getMessage());
//            }
//            reserva.setIdCliente(updateReserva.getIdCliente());
//        }

        // 3) Actualizar fecha/hora si vienen
        if (updateReserva.getFecha() != null) reserva.setFecha(updateReserva.getFecha());
        if (updateReserva.getHora()  != null) reserva.setHora(updateReserva.getHora());
        if (updateReserva.getIdCliente() != null) reserva.setIdCliente(updateReserva.getIdCliente());

        // 4) Si te mandan idMesa, cargar y asignar
        if (updateReserva.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(updateReserva.getIdMesa())
                    .orElseThrow(() -> new IllegalArgumentException("Mesa no existe"));
            // (Opcional) validar disponibilidad de la mesa/horario aquí si tienes ese método en el repo
            reserva.setMesa(mesa);
        }
        if (updateReserva.getEstado() != null) {
            reserva.setEstado(updateReserva.getEstado()); 
       }
        

        // 5) Guardar y responder
        Reserva saved = reservaRepository.save(reserva);
        return ReservaMapper.mapToReservaDto(saved);
    }

    @Transactional
    @Override
    public void deleteReserva(Integer reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }
        reservaRepository.deleteById(reservaId);
    }

	@Override
	public List<ReservaDto> getReservaDate(LocalDate fecha) {
		List<Reserva> reservas;

        if (fecha != null) {
            // Si se proporciona una fecha, filtra por esa fecha
            reservas = reservaRepository.findByFecha(fecha);
        } else {
            // Si no hay fecha, trae todas las reservas
            reservas = reservaRepository.findAll();
        }
        
        return reservas.stream()
                .map(ReservaMapper::mapToReservaDto)
                .collect(Collectors.toList());
	}
	/*private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    
    
    @Transactional
    @Override
    public ReservaDto createReserva(ReservaDto reservaDto) {
        if (reservaDto.getIdMesa() == null) {
            throw new IllegalArgumentException("idMesa es obligatorio");
        }
        if (reservaDto.getIdCliente() == null) {
            throw new IllegalArgumentException("idCliente es obligatorio");
        }

        Mesa mesa = mesaRepository.findById(reservaDto.getIdMesa())
                .orElseThrow(() -> new IllegalArgumentException("Mesa no existe"));

        Reserva reserva = ReservaMapper.mapToReserva(reservaDto);
        reserva.setMesa(mesa);

        Reserva saved = reservaRepository.save(reserva);
        return ReservaMapper.mapToReservaDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public ReservaDto getReservaById(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        return ReservaMapper.mapToReservaDto(reserva);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservaDto> getAllReserva() {
        return reservaRepository.findAll().stream()
                .map(ReservaMapper::mapToReservaDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReservaDto updateReserva(Integer reservaId, ReservaDto updateReserva) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (updateReserva.getFecha() != null) reserva.setFecha(updateReserva.getFecha());
        if (updateReserva.getHora()  != null) reserva.setHora(updateReserva.getHora());
        if (updateReserva.getIdCliente() != null) reserva.setIdCliente(updateReserva.getIdCliente());

        if (updateReserva.getIdMesa() != null) {
            Mesa mesa = mesaRepository.findById(updateReserva.getIdMesa())
                    .orElseThrow(() -> new IllegalArgumentException("Mesa no existe"));
            reserva.setMesa(mesa);
        }

        Reserva saved = reservaRepository.save(reserva);
        return ReservaMapper.mapToReservaDto(saved);
    }

    @Transactional
    @Override
    public void deleteReserva(Integer reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }
        reservaRepository.deleteById(reservaId);
    }*/
	
}
