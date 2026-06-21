package com.tiendasara.models;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoDto {
    
    private Integer id;
    
    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(min = 2, max = 150, message = "La descripción debe tener entre 2 y 150 caracteres")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer idCategoria;
    
    @NotNull(message = "La marca es obligatoria")
    private Integer idMarca;
    
    private String nombreCategoria;
    private String nombreMarca;
    
    // Constructores
    public ProductoDto() {}
    
    public ProductoDto(Integer id, String descripcion, BigDecimal precio, Integer cantidad, 
                      Integer idCategoria, Integer idMarca) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    
    public Integer getIdMarca() { return idMarca; }
    public void setIdMarca(Integer idMarca) { this.idMarca = idMarca; }
    
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    
    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) { this.nombreMarca = nombreMarca; }
}


