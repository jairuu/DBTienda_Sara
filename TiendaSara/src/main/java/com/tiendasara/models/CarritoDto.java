package com.tiendasara.models;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CarritoDto {
    
    private Integer id;
    
    @NotBlank(message = "El folio de venta es obligatorio")
    @Size(max = 20, message = "El folio no puede exceder 20 caracteres")
    private String folioVenta;
    
    private BigDecimal totalCompra;
    
    @NotBlank(message = "El estatus es obligatorio")
    @Size(max = 20, message = "El estatus no puede exceder 20 caracteres")
    private String estatus;
    
    private LocalDate fecha;
    private List<CarritoDetalleDto> detalles;
    
    // Constructores
    public CarritoDto() {}
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getFolioVenta() { return folioVenta; }
    public void setFolioVenta(String folioVenta) { this.folioVenta = folioVenta; }
    
    public BigDecimal getTotalCompra() { return totalCompra; }
    public void setTotalCompra(BigDecimal totalCompra) { this.totalCompra = totalCompra; }
    
    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public List<CarritoDetalleDto> getDetalles() { return detalles; }
    public void setDetalles(List<CarritoDetalleDto> detalles) { this.detalles = detalles; }
}
