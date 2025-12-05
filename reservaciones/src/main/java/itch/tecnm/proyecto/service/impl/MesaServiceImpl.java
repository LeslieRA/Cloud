package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.dto.MesaDto;
import itch.tecnm.proyecto.entity.Mesa;
import itch.tecnm.proyecto.mapper.MesaMapper;
import itch.tecnm.proyecto.repository.MesaRepository;
import itch.tecnm.proyecto.service.MesaService;
import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class MesaServiceImpl implements MesaService{
	private final MesaRepository mesaRepository;

    @Transactional
    @Override
    public MesaDto createMesa(MesaDto mesaDto) {
        Mesa mesa = MesaMapper.mapToMesa(mesaDto);
        Mesa saved = mesaRepository.save(mesa);
        return MesaMapper.mapToMesaDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public MesaDto getMesaById(Integer mesaId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
        return MesaMapper.mapToMesaDto(mesa);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MesaDto> getAllMesa() {
        return mesaRepository.findAll().stream()
                .map(MesaMapper::mapToMesaDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MesaDto updateMesa(Integer mesaId, MesaDto updateMesa) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        if (updateMesa.getNumero() != null)    mesa.setNumero(updateMesa.getNumero());
        if (updateMesa.getCapacidad() != null) mesa.setCapacidad(updateMesa.getCapacidad());
        if (updateMesa.getUbicacion() != null) mesa.setUbicacion(updateMesa.getUbicacion());

        Mesa saved = mesaRepository.save(mesa);
        return MesaMapper.mapToMesaDto(saved);
    }

    @Transactional
    @Override
    public void deleteMesa(Integer mesaId) {
        if (!mesaRepository.existsById(mesaId)) {
            throw new IllegalArgumentException("Mesa no encontrada");
        }
        mesaRepository.deleteById(mesaId);
    }

	@Override
	public MesaDto toggleEstadoMesa(Integer idMesa) {
		// 1. Busca la mesa o lanza un error si no la encuentra
        Mesa mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
        
        // 2. Invierte el estado actual (si era true, ahora es false, y viceversa)
        mesa.setEstado(!mesa.isEstado());
        
        // 3. Guarda la mesa actualizada en la base de datos
        Mesa mesaActualizada = mesaRepository.save(mesa);
        
        // 4. Devuelve el DTO con los datos actualizados
        return MesaMapper.mapToMesaDto(mesaActualizada);
    }

	@Override
	public List<MesaDto> getMesasPorEstado(boolean estado) {
		List<Mesa> mesas = mesaRepository.findByEstado(estado);
        return mesas.stream()
                    .map(MesaMapper::mapToMesaDto)
                    .collect(Collectors.toList());
	}
	
}
