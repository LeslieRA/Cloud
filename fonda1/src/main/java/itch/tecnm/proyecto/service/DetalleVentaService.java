package itch.tecnm.proyecto.service;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import itch.tecnm.proyecto.dto.DetalleVentaDto;
import jakarta.servlet.http.HttpServletResponse;

public interface DetalleVentaService {
	// Consulta un detalle por id
    DetalleVentaDto getDetalleById(Integer detalleId);

    // Lista todos los detalles
    List<DetalleVentaDto> getAllDetallesVenta();

    // Lista los detalles de una venta específica
    List<DetalleVentaDto> getDetallesByVenta(Integer idVenta);
    
   
}
