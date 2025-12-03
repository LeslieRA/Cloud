package itch.tecnm.proyecto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import itch.tecnm.proyecto.dto.TipoDto;
import itch.tecnm.proyecto.entity.Tipo;
import itch.tecnm.proyecto.mapper.TipoMapper;
import itch.tecnm.proyecto.repository.TipoRepository;
import itch.tecnm.proyecto.service.TipoService;
import lombok.AllArgsConstructor;

@Service
//Utilizando lombok para que ocupe todos los argumentos como constructor
@AllArgsConstructor
public class TipoServiceImpl implements TipoService{
private TipoRepository tipoRepository;
	
	@Override
	public TipoDto createTipo(TipoDto tipoDto) {
		Tipo tipo = TipoMapper.mapToTipo(tipoDto);
		Tipo savedTipo = tipoRepository.save(tipo);
		
		return TipoMapper.mapToTipoDto(savedTipo);
	}

	@Override
	public TipoDto getTipoById(Integer tipoId) {
		Tipo tipo = tipoRepository.findById(tipoId).orElse(null);
		return TipoMapper.mapToTipoDto(tipo);
	}

	@Override
	public List<TipoDto> getAllTipo() {
		List<Tipo> tipos = tipoRepository.findAll();
		
		return tipos.stream().map((tipo) -> TipoMapper.mapToTipoDto(tipo))
				.collect(Collectors.toList());
	}

	@Override
	public TipoDto updateTipo(Integer tipoId, TipoDto updateTipo) {
		//Buscamos el registro a modificar
		Tipo tipo = tipoRepository.findById(tipoId).orElse(null);
		//Modificar los atributos
		tipo.setTipo(updateTipo.getTipo());
		tipo.setDescripcion(updateTipo.getDescripcion());
		
		//Guardar cambios
		Tipo updateTipoObj = tipoRepository.save(tipo);
		
		return TipoMapper.mapToTipoDto(updateTipoObj);
	}

	@Override
	public void deleteTipo(Integer tipoId) {
		//Buscamos el registro que se desea eliminar
		Tipo tipo = tipoRepository.findById(tipoId).orElse(null);
		tipoRepository.deleteById(tipoId);
		
	}
}
