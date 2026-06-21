package com.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tiendasara.models.Marca;
import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    
    List<Marca> findByDescripcionContainingIgnoreCase(String descripcion);
    boolean existsByDescripcion(String descripcion);
}

