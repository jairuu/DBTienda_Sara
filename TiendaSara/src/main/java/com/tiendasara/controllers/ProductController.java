package com.tiendasara.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tiendasara.models.*;
import com.tiendasara.services.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoDto>> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDto> productoDtos = productos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productoDtos);
    }
    
    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> getProductoById(@PathVariable Integer id) {
        return productoRepository.findById(id)
            .map(producto -> ResponseEntity.ok(convertToDto(producto)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<ProductoDto> createProducto(@Valid @RequestBody ProductoDto productoDto) {
        Producto producto = convertToEntity(productoDto);
        Producto savedProducto = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(convertToDto(savedProducto));
    }
    
    // Actualizar producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> updateProducto(@PathVariable Integer id, 
                                                       @Valid @RequestBody ProductoDto productoDto) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Producto producto = convertToEntity(productoDto);
        producto.setId(id);
        Producto updatedProducto = productoRepository.save(producto);
        return ResponseEntity.ok(convertToDto(updatedProducto));
    }
    
    // Actualización parcial de producto
    @PatchMapping("/{id}")
    public ResponseEntity<ProductoDto> patchProducto(@PathVariable Integer id, 
                                                      @RequestBody ProductoDto productoDto) {
        return productoRepository.findById(id)
            .map(producto -> {
                if (productoDto.getDescripcion() != null) {
                    producto.setDescripcion(productoDto.getDescripcion());
                }
                if (productoDto.getPrecio() != null) {
                    producto.setPrecio(productoDto.getPrecio());
                }
                if (productoDto.getCantidad() != null) {
                    producto.setCantidad(productoDto.getCantidad());
                }
                if (productoDto.getIdCategoria() != null) {
                    categoriaRepository.findById(productoDto.getIdCategoria())
                        .ifPresent(producto::setCategoria);
                }
                if (productoDto.getIdMarca() != null) {
                    marcaRepository.findById(productoDto.getIdMarca())
                        .ifPresent(producto::setMarca);
                }
                
                Producto updatedProducto = productoRepository.save(producto);
                return ResponseEntity.ok(convertToDto(updatedProducto));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // Buscar productos por descripción
    @GetMapping("/search")
    public ResponseEntity<List<ProductoDto>> searchProductos(@RequestParam String descripcion) {
        List<Producto> productos = productoRepository.findByDescripcionContainingIgnoreCase(descripcion);
        List<ProductoDto> productoDtos = productos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productoDtos);
    }
    
    // Filtrar productos por categoría
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ProductoDto>> getProductosByCategoria(@PathVariable Integer idCategoria) {
        List<Producto> productos = productoRepository.findByCategoriaId(idCategoria);
        List<ProductoDto> productoDtos = productos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productoDtos);
    }
    
    // Filtrar productos por marca
    @GetMapping("/marca/{idMarca}")
    public ResponseEntity<List<ProductoDto>> getProductosByMarca(@PathVariable Integer idMarca) {
        List<Producto> productos = productoRepository.findByMarcaId(idMarca);
        List<ProductoDto> productoDtos = productos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productoDtos);
    }
    
    // Método de conversión de Entity a DTO
    private ProductoDto convertToDto(Producto producto) {
        ProductoDto dto = new ProductoDto(
            producto.getId(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getCantidad(),
            producto.getCategoria() != null ? producto.getCategoria().getId() : null,
            producto.getMarca() != null ? producto.getMarca().getId() : null
        );
        
        if (producto.getCategoria() != null) {
            dto.setNombreCategoria(producto.getCategoria().getDescripcion());
        }
        
        if (producto.getMarca() != null) {
            dto.setNombreMarca(producto.getMarca().getDescripcion());
        }
        
        return dto;
    }
    
    // Método de conversión de DTO a Entity
    private Producto convertToEntity(ProductoDto dto) {
        Producto producto = new Producto();
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        
        if (dto.getIdCategoria() != null) {
            categoriaRepository.findById(dto.getIdCategoria())
                .ifPresent(producto::setCategoria);
        }
        
        if (dto.getIdMarca() != null) {
            marcaRepository.findById(dto.getIdMarca())
                .ifPresent(producto::setMarca);
        }
        
        return producto;
    }
}
