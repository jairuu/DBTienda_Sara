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
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
public class MarkController {
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    @GetMapping
    public ResponseEntity<List<MarcaDto>> getAllMarcas() {
        List<Marca> marcas = marcaRepository.findAll();
        List<MarcaDto> marcaDtos = marcas.stream()
            .map(mar -> new MarcaDto(mar.getId(), mar.getDescripcion()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(marcaDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MarcaDto> getMarcaById(@PathVariable Integer id) {
        return marcaRepository.findById(id)
            .map(mar -> ResponseEntity.ok(new MarcaDto(mar.getId(), mar.getDescripcion())))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MarcaDto> createMarca(@Valid @RequestBody MarcaDto marcaDto) {
        Marca marca = new Marca(marcaDto.getDescripcion());
        Marca savedMarca = marcaRepository.save(marca);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MarcaDto(savedMarca.getId(), savedMarca.getDescripcion()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MarcaDto> updateMarca(@PathVariable Integer id, 
                                                 @Valid @RequestBody MarcaDto marcaDto) {
        if (!marcaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Marca marca = new Marca(marcaDto.getDescripcion());
        marca.setId(id);
        Marca updatedMarca = marcaRepository.save(marca);
        return ResponseEntity.ok(new MarcaDto(updatedMarca.getId(), updatedMarca.getDescripcion()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Integer id) {
        if (!marcaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        marcaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
