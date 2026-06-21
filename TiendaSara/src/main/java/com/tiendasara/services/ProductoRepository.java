package com.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tiendasara.models.Producto;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);
    List<Producto> findByCategoriaId(Integer idCategoria);
    List<Producto> findByMarcaId(Integer idMarca);
    List<Producto> findByPrecioBetween(BigDecimal minPrecio, BigDecimal maxPrecio);
    List<Producto> findByCantidadLessThan(Integer cantidad);
}
