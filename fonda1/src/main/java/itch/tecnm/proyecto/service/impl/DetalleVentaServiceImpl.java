package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.dto.DetalleVentaDto;
import itch.tecnm.proyecto.entity.DetalleVenta;
import itch.tecnm.proyecto.mapper.DetalleVentaMapper;
import itch.tecnm.proyecto.repository.DetalleVentaRepository;
import itch.tecnm.proyecto.service.DetalleVentaService;

import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class DetalleVentaServiceImpl implements DetalleVentaService{
	private final DetalleVentaRepository detalleVentaRepository;
	
	

    @Transactional(readOnly = true)
    @Override
    public DetalleVentaDto getDetalleById(Integer detalleId) {
        DetalleVenta dv = detalleVentaRepository.findById(detalleId)
                .orElseThrow(() -> new IllegalArgumentException("Detalle de venta no encontrado"));
        return DetalleVentaMapper.mapToDetalleVentaDto(dv);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DetalleVentaDto> getAllDetallesVenta() {
        return detalleVentaRepository.findAll().stream()
                .map(DetalleVentaMapper::mapToDetalleVentaDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<DetalleVentaDto> getDetallesByVenta(Integer idVenta) {
        return detalleVentaRepository.findByVenta_IdVenta(idVenta).stream()
                .map(DetalleVentaMapper::mapToDetalleVentaDto)
                .collect(Collectors.toList());
    }
}
