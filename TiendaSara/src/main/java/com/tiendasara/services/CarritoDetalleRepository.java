package com.tiendasara.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tiendasara.models.CarritoDetalle;
import java.util.List;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {
    
    List<CarritoDetalle> findByCarritoId(Integer idCarrito);
    List<CarritoDetalle> findByProductoId(Integer idProducto);
}