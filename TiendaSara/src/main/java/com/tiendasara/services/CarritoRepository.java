package com.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tiendasara.models.Carrito;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    
    List<Carrito> findByEstatus(String estatus);
    List<Carrito> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Carrito> findByFolioVentaContaining(String folioVenta);
}
