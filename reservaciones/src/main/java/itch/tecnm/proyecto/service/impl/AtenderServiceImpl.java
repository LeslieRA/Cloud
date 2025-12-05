package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itch.tecnm.proyecto.client.VentaClient;
import itch.tecnm.proyecto.dto.AtenderDto;
import itch.tecnm.proyecto.entity.Atender;
import itch.tecnm.proyecto.mapper.AtenderMapper;
import itch.tecnm.proyecto.repository.AtenderRepository;
import itch.tecnm.proyecto.repository.EmpleadoRepository;
import itch.tecnm.proyecto.service.AtenderService;
import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class AtenderServiceImpl implements AtenderService {

  private final AtenderRepository atenderRepository;
  private final EmpleadoRepository empleadoRepository;
  // private final VentaClient ventaClient; // Feign hacia Fonda

  @Transactional
  @Override
  public AtenderDto createAtender(AtenderDto dto) {
      if (dto.getIdEmpleado() == null) throw new IllegalArgumentException("idEmpleado es obligatorio");
      if (dto.getIdVenta() == null)    throw new IllegalArgumentException("idVenta es obligatorio");

      /*
      // Validar VENTA en Fonda
      try {
          var venta = ventaClient.findById(dto.getIdVenta());
          if (venta == null || venta.getIdVenta() == null) {
              throw new IllegalArgumentException("Venta no existe: " + dto.getIdVenta());
          }
      } catch (feign.FeignException.NotFound nf) {
          throw new IllegalArgumentException("Venta no existe: " + dto.getIdVenta());
      } catch (feign.FeignException fe) {
          throw new IllegalStateException("Error consultando Fonda: " + fe.getMessage());
      }
      */

      // Validar EMPLEADO local
      var empleado = empleadoRepository.findById(dto.getIdEmpleado())
              .orElseThrow(() -> new IllegalArgumentException("Empleado no existe"));

      var entidad = AtenderMapper.mapToAtender(dto);
      entidad.setEmpleado(empleado);
      var guardada = atenderRepository.save(entidad);
      return AtenderMapper.mapToAtenderDto(guardada);
  }

  @Transactional
  @Override
  public AtenderDto updateAtender(Integer atenderId, AtenderDto update) {
      var atender = atenderRepository.findById(atenderId)
              .orElseThrow(() -> new IllegalArgumentException("Atención no encontrada"));

      // Cambiar empleado (si viene)
      if (update.getIdEmpleado() != null &&
          (atender.getEmpleado() == null || !update.getIdEmpleado().equals(atender.getEmpleado().getIdEmpleado()))) {
          var empleado = empleadoRepository.findById(update.getIdEmpleado())
                  .orElseThrow(() -> new IllegalArgumentException("Empleado no existe"));
          atender.setEmpleado(empleado);
      }

      /*
      // Cambiar venta (si viene)
      if (update.getIdVenta() != null && !update.getIdVenta().equals(atender.getIdVenta())) {
          try {
              var venta = ventaClient.findById(update.getIdVenta());
              if (venta == null || venta.getIdVenta() == null) {
                  throw new IllegalArgumentException("Venta no existe: " + update.getIdVenta());
              }
          } catch (feign.FeignException.NotFound nf) {
              throw new IllegalArgumentException("Venta no existe: " + update.getIdVenta());
          } catch (feign.FeignException fe) {
              throw new IllegalStateException("Error consultando Fonda: " + fe.getMessage());
          }
          atender.setIdVenta(update.getIdVenta());
      }
      */

      var saved = atenderRepository.save(atender);
      return AtenderMapper.mapToAtenderDto(saved);
  }
  
  /*private final AtenderRepository atenderRepository;
  private final EmpleadoRepository empleadoRepository;

  @Transactional
  @Override
  public AtenderDto createAtender(AtenderDto atenderDto) {
      if (atenderDto.getIdEmpleado() == null) {
          throw new IllegalArgumentException("idEmpleado es obligatorio");
      }
      Empleado empleado = empleadoRepository.findById(atenderDto.getIdEmpleado())
              .orElseThrow(() -> new IllegalArgumentException("Empleado no existe"));

      Atender atender = AtenderMapper.mapToAtender(atenderDto);
      atender.setEmpleado(empleado); // asegurar entidad administrada

      Atender saved = atenderRepository.save(atender);
      return AtenderMapper.mapToAtenderDto(saved);
  }
  */

  @Transactional(readOnly = true)
  @Override
  public AtenderDto getAtenderById(Integer atenderId) {
      Atender atender = atenderRepository.findById(atenderId)
              .orElseThrow(() -> new IllegalArgumentException("Atención no encontrada"));
      return AtenderMapper.mapToAtenderDto(atender);
  }

  @Transactional(readOnly = true)
  @Override
  public List<AtenderDto> getAllAtender() {
      return atenderRepository.findAll().stream()
              .map(AtenderMapper::mapToAtenderDto)
              .collect(Collectors.toList());
  }
  
  /*
  @Transactional
  @Override
  public AtenderDto updateAtender(Integer atenderId, AtenderDto updateAtender) {
      Atender atender = atenderRepository.findById(atenderId)
              .orElseThrow(() -> new IllegalArgumentException("Atención no encontrada"));

      if (updateAtender.getIdEmpleado() != null) {
          Empleado empleado = empleadoRepository.findById(updateAtender.getIdEmpleado())
                  .orElseThrow(() -> new IllegalArgumentException("Empleado no existe"));
          atender.setEmpleado(empleado);
      }
      if (updateAtender.getIdVenta() != null) {
          atender.setIdVenta(updateAtender.getIdVenta());
      }

      Atender saved = atenderRepository.save(atender);
      return AtenderMapper.mapToAtenderDto(saved);
  }
  */

  @Transactional
  @Override
  public void deleteAtender(Integer atenderId) {
      if (!atenderRepository.existsById(atenderId)) {
          throw new IllegalArgumentException("Atención no encontrada");
      }
      atenderRepository.deleteById(atenderId);
  }
}