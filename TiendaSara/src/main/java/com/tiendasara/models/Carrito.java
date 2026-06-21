package com.tiendasara.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Carrito")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    
    @NotBlank(message = "El folio de venta es obligatorio")
    @Size(max = 20, message = "El folio no puede exceder 20 caracteres")
    @Column(name = "FolioVenta", nullable = false, length = 20)
    private String folioVenta;
    
    @NotNull(message = "El total de compra es obligatorio")
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    @Column(name = "TotalCompra", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCompra;
    
    @NotBlank(message = "El estatus es obligatorio")
    @Size(max = 20, message = "El estatus no puede exceder 20 caracteres")
    @Column(name = "Estatus", nullable = false, length = 20)
    private String estatus;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarritoDetalle> detalles = new ArrayList<>();
    
    // Constructores
    public Carrito() {}
    
    public Carrito(String folioVenta, BigDecimal totalCompra, String estatus, LocalDate fecha) {
        this.folioVenta = folioVenta;
        this.totalCompra = totalCompra;
        this.estatus = estatus;
        this.fecha = fecha;
    }
    
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
    
    public List<CarritoDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<CarritoDetalle> detalles) { this.detalles = detalles; }
}