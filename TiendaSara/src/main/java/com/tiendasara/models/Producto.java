package com.tiendasara.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    
    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(min = 2, max = 150, message = "La descripción debe tener entre 2 y 150 caracteres")
    @Column(name = "Descripcion", nullable = false, length = 150)
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;
    
    @ManyToOne(fetch = FetchType.EAGER)  // Cambiado a EAGER para evitar problemas de lazy loading
    @JoinColumn(name = "idCategoria", referencedColumnName = "Id", nullable = false)
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.EAGER)  // Cambiado a EAGER para evitar problemas de lazy loading
    @JoinColumn(name = "idMarca", referencedColumnName = "Id", nullable = false)
    private Marca marca;
    
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<CarritoDetalle> carritoDetalles = new ArrayList<>();
    
    // Constructores
    public Producto() {}
    
    public Producto(String descripcion, BigDecimal precio, Integer cantidad) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
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
    
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    
    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
    
    public List<CarritoDetalle> getCarritoDetalles() { return carritoDetalles; }
    public void setCarritoDetalles(List<CarritoDetalle> carritoDetalles) { this.carritoDetalles = carritoDetalles; }
}


