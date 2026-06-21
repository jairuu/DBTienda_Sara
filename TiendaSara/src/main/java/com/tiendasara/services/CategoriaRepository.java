package com.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tiendasara.models.Categoria;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    List<Categoria> findByDescripcionContainingIgnoreCase(String descripcion);
    boolean existsByDescripcion(String descripcion);
}


