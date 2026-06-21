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
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> getAllCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaDto> categoriaDtos = categorias.stream()
            .map(cat -> new CategoriaDto(cat.getId(), cat.getDescripcion()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(categoriaDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> getCategoriaById(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
            .map(cat -> ResponseEntity.ok(new CategoriaDto(cat.getId(), cat.getDescripcion())))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CategoriaDto> createCategoria(@Valid @RequestBody CategoriaDto categoriaDto) {
        Categoria categoria = new Categoria(categoriaDto.getDescripcion());
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CategoriaDto(savedCategoria.getId(), savedCategoria.getDescripcion()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> updateCategoria(@PathVariable Integer id, 
                                                         @Valid @RequestBody CategoriaDto categoriaDto) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Categoria categoria = new Categoria(categoriaDto.getDescripcion());
        categoria.setId(id);
        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(new CategoriaDto(updatedCategoria.getId(), updatedCategoria.getDescripcion()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
