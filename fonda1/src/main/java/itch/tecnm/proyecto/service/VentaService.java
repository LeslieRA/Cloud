package itch.tecnm.proyecto.service;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import itch.tecnm.proyecto.dto.VentaDto;
import jakarta.servlet.http.HttpServletResponse;

public interface VentaService {
	// Crea la venta con su lista de detalles; calcula total y asigna fechaVenta si viene null
    VentaDto createVenta(VentaDto ventaDto);

    // Consulta por id
    VentaDto getVentaById(Integer ventaId);

    // Lista todas
    List<VentaDto> getAllVentas();

    // Actualiza la venta (incluyendo reemplazar/ajustar detalles)
    VentaDto updateVenta(Integer ventaId, VentaDto updateVenta);

    // Elimina la venta (y sus detalles por cascade)
    void deleteVenta(Integer ventaId);
    
 // --- 👇 AÑADE ESTA FIRMA PARA EL TICKET 👇 ---
    void generarTicketPdf(Integer idVenta, HttpServletResponse response) 
        throws IOException, DocumentException;
}
