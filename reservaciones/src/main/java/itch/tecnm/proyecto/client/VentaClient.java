package itch.tecnm.proyecto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@FeignClient(name = "fonda1", path = "/api/venta")
public interface VentaClient {
    @GetMapping("/{id}")
    VentaDto findById(@PathVariable("id") Integer id);

    @JsonIgnoreProperties(ignoreUnknown = true)
    class VentaDto {
        @JsonAlias({"idVenta","id_venta"})
        private Integer idVenta;
        public Integer getIdVenta() { return idVenta; }
        public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }
    }
}
