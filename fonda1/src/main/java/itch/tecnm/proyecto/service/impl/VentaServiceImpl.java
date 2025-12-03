package itch.tecnm.proyecto.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// --- Imports de iText (versión 5) CORREGIDOS ---
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle; // <-- ¡CORREGIDO!
import com.itextpdf.text.pdf.PdfContentByte; // <-- AÑADIDO
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import feign.FeignException;
import itch.tecnm.proyecto.client.ClienteClient.ClienteDto;
// Importa todos tus DTOs
import itch.tecnm.proyecto.dto.AtenderDto;
import itch.tecnm.proyecto.dto.DetalleVentaDto;
import itch.tecnm.proyecto.dto.EmpleadoDto;
import itch.tecnm.proyecto.dto.MesaDto;
import itch.tecnm.proyecto.dto.ReservaDto;
import itch.tecnm.proyecto.dto.VentaDto;
import itch.tecnm.proyecto.entity.DetalleVenta;
import itch.tecnm.proyecto.entity.Producto;
import itch.tecnm.proyecto.entity.Venta;
import itch.tecnm.proyecto.feign.AtenderFeign;
import itch.tecnm.proyecto.feign.ClienteFeign;
import itch.tecnm.proyecto.feign.EmpleadoFeign;
import itch.tecnm.proyecto.feign.MesaFeign;
import itch.tecnm.proyecto.feign.ReservaFeign;
import itch.tecnm.proyecto.mapper.VentaMapper;
import itch.tecnm.proyecto.repository.ProductoRepository;
import itch.tecnm.proyecto.repository.VentaRepository;
import itch.tecnm.proyecto.service.VentaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VentaServiceImpl implements VentaService {
	
	private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    
    // --- Inyección de los Clientes Feign ---
    private final ClienteFeign clienteFeign;
    private final EmpleadoFeign empleadoFeign;
    private final ReservaFeign reservaFeign;
    private final MesaFeign mesaFeign;
    private final AtenderFeign atenderFeign;

    // --- MÉTODOS CRUD (LOS TUYOS) ---
    @Override
    public VentaDto createVenta(VentaDto ventaDto) {
        if (ventaDto.getIdCliente() == null) {
            throw new IllegalArgumentException("idCliente es obligatorio");
        }
        // ... (Validación de Cliente Feign comentada) ...
        Venta venta = VentaMapper.mapToVenta(ventaDto);
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle");
        }
        double total = 0.0;
        for (DetalleVenta det : venta.getDetalles()) {
            det.setVenta(venta);
            if (det.getCantidad() == null || det.getCantidad() <= 0) det.setCantidad(1);
            Integer idProd = (det.getProducto() != null) ? det.getProducto().getId_producto() : null;
            if (idProd == null) throw new IllegalArgumentException("idProducto es obligatorio en cada detalle");
            if (det.getPrecioUnitario() == null || det.getPrecioUnitario() <= 0) {
                Producto p = productoRepository.findById(idProd)
                        .orElseThrow(() -> new IllegalArgumentException("Producto no existe: " + idProd));
                det.setPrecioUnitario(p.getPrecioProducto());
            }
            total += det.getCantidad() * det.getPrecioUnitario();
        }
        venta.setTotal(total);
        Venta guardada = ventaRepository.save(venta);
        return VentaMapper.mapToVentaDto(guardada);
    }

    @Override
    public VentaDto updateVenta(Integer ventaId, VentaDto updateVenta) {
    	Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        // 1) Cambiar cliente (si viene y cambió)
        if (updateVenta.getIdCliente() != null && !updateVenta.getIdCliente().equals(venta.getIdCliente())) {
            try {
              //  clienteClient.findById(updateVenta.getIdCliente());
            } catch (FeignException.NotFound nf) {
                throw new IllegalArgumentException("Cliente no existe: " + updateVenta.getIdCliente());
            } catch (FeignException fe) {
                throw new IllegalStateException("Error al consultar Restaurante: " + fe.getMessage());
            }
            venta.setIdCliente(updateVenta.getIdCliente());
        }

        if (venta.getDetalles() == null) {
            venta.setDetalles(new ArrayList<>());
        }

        // 2) Indexar detalles existentes por id
        Map<Integer, DetalleVenta> existentes = new HashMap<>();
        for (DetalleVenta d : venta.getDetalles()) {
            // Ajusta el nombre del getter según tu entidad: getIdDetalle() vs getIdDetalleVenta()
            existentes.put(d.getIdDetalle(), d);
        }

        double total;

        if (updateVenta.getDetalles() != null) {
            total = 0.0;

            for (DetalleVentaDto dDto : updateVenta.getDetalles()) {
                // a) Producto
                Integer idProd = dDto.getIdProducto();
                if (idProd == null) throw new IllegalArgumentException("idProducto es obligatorio en cada detalle");

                Producto prod = productoRepository.findById(idProd)
                        .orElseThrow(() -> new IllegalArgumentException("Producto no existe: " + idProd));

                // b) Cantidad y precio
                int cantidad = (dDto.getCantidad() == null || dDto.getCantidad() <= 0) ? 1 : dDto.getCantidad();
                Double precio = dDto.getPrecioUnitario();
                if (precio == null || precio <= 0) precio = prod.getPrecioProducto();
                if (precio == null || precio <= 0) throw new IllegalArgumentException("precioUnitario inválido");

                // c) Actualizar o crear
                if (dDto.getIdDetalle() != null) { // ajusta el nombre del campo si es idDetalleVenta
                    DetalleVenta existente = existentes.remove(dDto.getIdDetalle());
                    if (existente == null) {
                        throw new IllegalArgumentException("El detalle " + dDto.getIdDetalle() + " no existe en esta venta");
                    }
                    existente.setProducto(prod);
                    existente.setCantidad(cantidad);
                    existente.setPrecioUnitario(precio);
                } else {
                    DetalleVenta nuevo = new DetalleVenta();
                    nuevo.setVenta(venta);
                    nuevo.setProducto(prod);
                    nuevo.setCantidad(cantidad);
                    nuevo.setPrecioUnitario(precio);
                    venta.getDetalles().add(nuevo);
                }

                total += cantidad * precio;
            }

            // 3) Eliminar los que ya no vinieron en el DTO
            for (DetalleVenta aEliminar : existentes.values()) {
                venta.getDetalles().remove(aEliminar); // orphanRemoval los borra en DB
            }

        } else {
            // 🔴 FIX: Si no mandan detalles en el update, **no poner total=0**.
            // Recalcular desde los detalles que ya tiene la venta.
            total = venta.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                    .sum();
        }

        // 4) Total final
        venta.setTotal(total);

        Venta guardada = ventaRepository.save(venta);
        return VentaMapper.mapToVentaDto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDto getVentaById(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));
        return VentaMapper.mapToVentaDto(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDto> getAllVentas() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::mapToVentaDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVenta(Integer ventaId) {
        if (!ventaRepository.existsById(ventaId)) {
            throw new IllegalArgumentException("Venta no encontrada");
        }
        ventaRepository.deleteById(ventaId);
    }

    // ==========================================================
    // 🧾 GENERAR TICKET PDF (LÓGICA CORREGIDA)
    // ==========================================================
    @Transactional(readOnly = true)
    @Override
    public void generarTicketPdf(Integer idVenta, HttpServletResponse response)
            throws IOException, DocumentException {

        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la venta " + idVenta));
        List<DetalleVenta> detalles = venta.getDetalles();
        if (detalles.isEmpty()) throw new IllegalArgumentException("La venta no tiene detalles");

        // 2️⃣ "Investigar": Obtener cliente
        ClienteDto cliente = null;
        try {
            ResponseEntity<ClienteDto> clienteResponse = clienteFeign.getClienteById(venta.getIdCliente());
            if (clienteResponse.getStatusCode().is2xxSuccessful()) {
                cliente = clienteResponse.getBody();
            }
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo obtener Cliente: " + e.getMessage());
        }
        
        // 3️⃣ "Investigar": Obtener empleado
        EmpleadoDto empleado = null;
        try {
            ResponseEntity<AtenderDto> atenderResponse = atenderFeign.getAtenderByIdVenta(idVenta);
            if (atenderResponse.getStatusCode().is2xxSuccessful() && atenderResponse.getBody() != null) {
                AtenderDto atender = atenderResponse.getBody();
                ResponseEntity<EmpleadoDto> empleadoResponse = empleadoFeign.getEmpleadoById(atender.getIdEmpleado());
                if (empleadoResponse.getStatusCode().is2xxSuccessful()) {
                    empleado = empleadoResponse.getBody();
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo obtener Empleado: " + e.getMessage());
        }

        // 4️⃣ "Investigar": Obtener reserva y mesa
        String mesaNumero = "—";
        if (venta.getIdReserva() != null) {
            try {
                ResponseEntity<ReservaDto> reservaResponse = reservaFeign.getReservaById(venta.getIdReserva());
                if (reservaResponse.getStatusCode().is2xxSuccessful() && reservaResponse.getBody() != null) {
                    ReservaDto reserva = reservaResponse.getBody();
                    if (reserva.getIdMesa() != null) {
                        ResponseEntity<MesaDto> mesaResponse = mesaFeign.getMesaById(reserva.getIdMesa());
                        if (mesaResponse.getStatusCode().is2xxSuccessful() && mesaResponse.getBody() != null) {
                            MesaDto mesa = mesaResponse.getBody();
                            mesaNumero = (mesa.getNumero() != null) ? mesa.getNumero().toString() : "—";
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo obtener Mesa: " + e.getMessage());
            }
        }

        // 5️⃣ Configuración del PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=ticket_" + idVenta + ".pdf");
        Document doc = new Document(new Rectangle(270, 500), 15, 15, 15, 15);
        PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());

        // 🎨 Fondo color #e9ebe7
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                PdfContentByte canvas = writer.getDirectContentUnder();
                Rectangle rect = document.getPageSize();
                canvas.setColorFill(new BaseColor(233, 235, 231));
                canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
                canvas.fill();
            }
        });

        doc.open();

        // ===============================
        // 🖼️ LOGO
        // ===============================
        try {
            Image logo = Image.getInstance(getClass().getResource("/static/images/logo2.png")); 
            logo.scaleToFit(70, 70);
            logo.setAlignment(Image.ALIGN_CENTER);
            doc.add(logo);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
        }

        // ===============================
        // 🧾 ENCABEZADO
        // ===============================
        Font fTituloCafe = FontFactory.getFont("Helvetica-Bold", 14, BaseColor.BLACK);
        Paragraph cafeTitulo = new Paragraph("Café del Sol ☕", fTituloCafe);
        cafeTitulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(cafeTitulo);
        
        Font fTituloVenta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Font fSmall = FontFactory.getFont(FontFactory.HELVETICA, 7);

        Paragraph titulo = new Paragraph("Ticket de Venta #" + idVenta, fTituloVenta);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        doc.add(new Paragraph("----------------------------------------", fNormal));
        doc.add(new Paragraph("Cliente: " + (cliente != null ? cliente.getNombreCliente() : "N/A"), fNormal));
        doc.add(new Paragraph("Mesero: " + (empleado != null ? empleado.getIdEmpleado() : "N/A"), fNormal));
        doc.add(new Paragraph("Mesa: " + mesaNumero, fNormal));
        doc.add(new Paragraph("Fecha: " + venta.getFechaVenta().toString(), fNormal));
        doc.add(Chunk.NEWLINE);

        // ===============================
        // 🛒 TABLA DE PRODUCTOS
        // ===============================
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{1.5f, 4f, 2f, 2.5f});
        tabla.setWidthPercentage(100);
        
        tabla.addCell(crearCeldaHeader("Cant.", fTituloVenta));
        tabla.addCell(crearCeldaHeader("Producto", fTituloVenta));
        tabla.addCell(crearCeldaHeader("Precio", fTituloVenta));
        tabla.addCell(crearCeldaHeader("Subtotal", fTituloVenta));
        tabla.setHeaderRows(1);

        for (DetalleVenta d : detalles) {
            tabla.addCell(crearCeldaDato(String.valueOf(d.getCantidad()), fNormal, Element.ALIGN_CENTER));
            tabla.addCell(crearCeldaDato(d.getProducto().getNombreProducto(), fSmall, Element.ALIGN_LEFT));
            tabla.addCell(crearCeldaDato(String.format("$%.2f", d.getPrecioUnitario()), fNormal, Element.ALIGN_RIGHT));
            double subtotal = d.getCantidad() * d.getPrecioUnitario();
            tabla.addCell(crearCeldaDato(String.format("$%.2f", subtotal), fNormal, Element.ALIGN_RIGHT));
        }
        doc.add(tabla);
        doc.add(new Paragraph("----------------------------------------", fNormal));
        
        // ===============================
        // 💰 TOTAL
        // ===============================
        Paragraph totalP = new Paragraph(String.format("TOTAL: $%.2f", venta.getTotal()), fTituloVenta);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        doc.add(totalP);
        doc.add(Chunk.NEWLINE);

        Paragraph gracias = new Paragraph("¡Gracias por su compra!", fNormal);
        gracias.setAlignment(Element.ALIGN_CENTER);
        doc.add(gracias);

        doc.close();
    }
    
    // --- Métodos Ayudantes para la Tabla ---
    
    private PdfPCell crearCeldaHeader(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setBorderWidthBottom(1f);
        celda.setBorderColorBottom(BaseColor.DARK_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrado para encabezados
        celda.setPaddingBottom(5);
        celda.setPaddingTop(5);
        return celda;
    }

    private PdfPCell crearCeldaDato(String texto, Font fuente, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(3);
        return celda;
    }
}
