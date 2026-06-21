package com.tiendasara.models;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CarritoDetalleDto {
    
    private Integer id;
    
    @NotNull(message = "El ID del carrito es obligatorio")
    private Integer idCarrito;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Integer idProducto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    private BigDecimal subtotal;
    private String descripcionProducto;
    
    // Constructores
    public CarritoDetalleDto() {}
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Integer idCarrito) { this.idCarrito = idCarrito; }
    
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
}