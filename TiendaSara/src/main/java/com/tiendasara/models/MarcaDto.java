package com.tiendasara.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MarcaDto {
    
    private Integer id;
    
    @NotBlank(message = "La descripción de la marca es obligatoria")
    @Size(min = 2, max = 100, message = "La descripción debe tener entre 2 y 100 caracteres")
    private String descripcion;
    
    // Constructores
    public MarcaDto() {}
    
    public MarcaDto(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

