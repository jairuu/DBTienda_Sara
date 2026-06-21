package com.tiendasara.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tiendasara.models.*;
import com.tiendasara.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping
    public ResponseEntity<List<CarritoDto>> getAllCarritos() {
        List<Carrito> carritos = carritoRepository.findAll();
        List<CarritoDto> carritoDtos = carritos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(carritoDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDto> getCarritoById(@PathVariable Integer id) {
        return carritoRepository.findById(id)
            .map(carrito -> ResponseEntity.ok(convertToDto(carrito)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CarritoDto> createCarrito(@Valid @RequestBody CarritoDto carritoDto) {
        Carrito carrito = convertToEntity(carritoDto);
        
        // Si no se proporciona fecha, usar la fecha actual
        if (carrito.getFecha() == null) {
            carrito.setFecha(LocalDate.now());
        }
        
        // Si no se proporciona estatus, usar "Activo" por defecto
        if (carrito.getEstatus() == null || carrito.getEstatus().isEmpty()) {
            carrito.setEstatus("Activo");
        }
        
        // Generar folio de venta automáticamente si no se proporciona
        if (carrito.getFolioVenta() == null || carrito.getFolioVenta().isEmpty()) {
            carrito.setFolioVenta("FOL-" + System.currentTimeMillis());
        }
        
        Carrito savedCarrito = carritoRepository.save(carrito);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedCarrito));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CarritoDto> updateCarrito(@PathVariable Integer id, 
                                                     @Valid @RequestBody CarritoDto carritoDto) {
        if (!carritoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Carrito carrito = convertToEntity(carritoDto);
        carrito.setId(id);
        Carrito updatedCarrito = carritoRepository.save(carrito);
        return ResponseEntity.ok(convertToDto(updatedCarrito));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Integer id) {
        if (!carritoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        carritoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // Agregar detalle al carrito
    @PostMapping("/{idCarrito}/detalles")
    public ResponseEntity<CarritoDetalleDto> addDetalle(@PathVariable Integer idCarrito, 
                                                         @Valid @RequestBody CarritoDetalleDto detalleDto) {
        return carritoRepository.findById(idCarrito)
            .map(carrito -> {
                return productoRepository.findById(detalleDto.getIdProducto())
                    .map(producto -> {
                        CarritoDetalle detalle = new CarritoDetalle();
                        detalle.setCarrito(carrito);
                        detalle.setProducto(producto);
                        detalle.setCantidad(detalleDto.getCantidad());
                        
                        // Calcular subtotal
                        BigDecimal subtotal = producto.getPrecio()
                            .multiply(BigDecimal.valueOf(detalleDto.getCantidad()));
                        detalle.setSubtotal(subtotal);
                        
                        CarritoDetalle savedDetalle = carritoDetalleRepository.save(detalle);
                        
                        // Actualizar total del carrito
                        actualizarTotalCarrito(carrito);
                        
                        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(convertDetalleToDto(savedDetalle));
                    })
                    .orElse(ResponseEntity.notFound().build());
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Método para actualizar el total del carrito
    private void actualizarTotalCarrito(Carrito carrito) {
        List<CarritoDetalle> detalles = carritoDetalleRepository.findByCarritoId(carrito.getId());
        BigDecimal total = detalles.stream()
            .map(CarritoDetalle::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        carrito.setTotalCompra(total);
        carritoRepository.save(carrito);
    }
    
    private CarritoDto convertToDto(Carrito carrito) {
        CarritoDto dto = new CarritoDto();
        dto.setId(carrito.getId());
        dto.setFolioVenta(carrito.getFolioVenta());
        dto.setTotalCompra(carrito.getTotalCompra());
        dto.setEstatus(carrito.getEstatus());
        dto.setFecha(carrito.getFecha());
        
        List<CarritoDetalleDto> detallesDto = carritoDetalleRepository
            .findByCarritoId(carrito.getId())
            .stream()
            .map(this::convertDetalleToDto)
            .collect(Collectors.toList());
        dto.setDetalles(detallesDto);
        
        return dto;
    }
    
    private CarritoDetalleDto convertDetalleToDto(CarritoDetalle detalle) {
        CarritoDetalleDto dto = new CarritoDetalleDto();
        dto.setId(detalle.getId());
        dto.setIdCarrito(detalle.getCarrito().getId());
        dto.setIdProducto(detalle.getProducto().getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setSubtotal(detalle.getSubtotal());
        dto.setDescripcionProducto(detalle.getProducto().getDescripcion());
        return dto;
    }
    
    private Carrito convertToEntity(CarritoDto dto) {
        Carrito carrito = new Carrito();
        carrito.setFolioVenta(dto.getFolioVenta());
        carrito.setTotalCompra(dto.getTotalCompra());
        carrito.setEstatus(dto.getEstatus());
        carrito.setFecha(dto.getFecha());
        return carrito;
    }
}
