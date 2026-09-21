package com.devgear.ms_productos.service;

import com.devgear.ms_productos.dto.ProductoRequestDTO;
import com.devgear.ms_productos.dto.ProductoResponseDTO;
import com.devgear.ms_productos.model.Producto;
import com.devgear.ms_productos.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Listado paginado de productos activos
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarPaginado(Pageable pageable) {
        return productoRepository.findByActivoTrue(pageable)
                .map(this::mapToDTO);
    }

    // Obtener un producto activo por ID
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return mapToDTO(producto);
    }

    // Crear un nuevo producto
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Producto producto = new Producto(
                dto.nombre(),
                dto.descripcion(),
                dto.precio(),
                dto.stock(),
                dto.categoria(),
                dto.imagenUrl()
        );
        Producto guardado = productoRepository.save(producto);
        return mapToDTO(guardado);
    }

    // Actualizar producto existente
    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        producto.setCategoria(dto.categoria());
        if (dto.imagenUrl() != null) {
            producto.setImagenUrl(dto.imagenUrl());
        }

        return mapToDTO(productoRepository.save(producto));
    }

    // Modificación parcial de stock
    @Transactional
    public ProductoResponseDTO modificarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock resultante no puede ser negativo");
        }

        producto.setStock(nuevoStock);
        return mapToDTO(productoRepository.save(producto));
    }

    // Borrado lógico (Desactivar)
    @Transactional
    public void desactivar(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // Mapeo privado de Entidad -> DTO
    private ProductoResponseDTO mapToDTO(Producto p) {
        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria(),
                p.getImagenUrl(),
                p.getActivo()
        );
    }
}