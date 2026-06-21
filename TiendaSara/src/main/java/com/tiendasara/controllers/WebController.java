package com.tiendasara.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.tiendasara.models.*;
import com.tiendasara.services.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web")
public class WebController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    // Página principal
    @GetMapping
    public String index() {
        return "index";
    }
    
    // Listar todos los productos
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de Productos");
        return "productos/lista";
    }
    
    // Mostrar formulario para crear producto
    @GetMapping("/productos/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("productoDto", new ProductoDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
        model.addAttribute("titulo", "Nuevo Producto");
        model.addAttribute("modo", "crear");
        return "productos/formulario";
    }
    
    // Guardar nuevo producto
    @PostMapping("/productos/guardar")
    public String guardarProducto(@Valid @ModelAttribute("productoDto") ProductoDto productoDto,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("marcas", marcaRepository.findAll());
            model.addAttribute("titulo", productoDto.getId() != null ? "Editar Producto" : "Nuevo Producto");
            model.addAttribute("modo", productoDto.getId() != null ? "editar" : "crear");
            return "productos/formulario";
        }
        
        try {
            Producto producto = convertirDtoAEntity(productoDto);
            if (productoDto.getId() != null) {
                producto.setId(productoDto.getId());
            }
            productoRepository.save(producto);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                productoDto.getId() != null ? "Producto actualizado exitosamente" : "Producto creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/web/productos";
    }
    
    // Mostrar formulario para editar producto
    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return productoRepository.findById(id)
            .map(producto -> {
                ProductoDto productoDto = convertirEntityADto(producto);
                model.addAttribute("productoDto", productoDto);
                model.addAttribute("categorias", categoriaRepository.findAll());
                model.addAttribute("marcas", marcaRepository.findAll());
                model.addAttribute("titulo", "Editar Producto");
                model.addAttribute("modo", "editar");
                return "productos/formulario";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/web/productos";
            });
    }
    
    // Ver detalle de producto
    @GetMapping("/productos/ver/{id}")
    public String verProducto(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return productoRepository.findById(id)
            .map(producto -> {
                model.addAttribute("producto", producto);
                model.addAttribute("titulo", "Detalle del Producto");
                return "productos/ver";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/web/productos";
            });
    }
    
    // Eliminar producto
    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            if (productoRepository.existsById(id)) {
                productoRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/web/productos";
    }
    
    // Métodos de conversión
    private ProductoDto convertirEntityADto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCantidad(producto.getCantidad());
        dto.setIdCategoria(producto.getCategoria() != null ? producto.getCategoria().getId() : null);
        dto.setIdMarca(producto.getMarca() != null ? producto.getMarca().getId() : null);
        return dto;
    }
    
    private Producto convertirDtoAEntity(ProductoDto dto) {
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
