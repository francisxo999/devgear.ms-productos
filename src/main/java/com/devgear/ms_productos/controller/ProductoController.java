package com.devgear.ms_productos.controller;

<<<<<<< Updated upstream
import com.devgear.ms_productos.model.Producto;
import com.devgear.ms_productos.repository.ProductoRepository;
=======
import com.devgear.ms_productos.dto.ProductoRequestDTO;
import com.devgear.ms_productos.dto.ProductoResponseDTO;
import com.devgear.ms_productos.dto.StockUpdateDTO;
import com.devgear.ms_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Consulta paginada (Catálogo para clientes / público)
    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductos(
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarPaginado(pageable));
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // Crear producto (Solo Administradores)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    // Actualización completa (Solo Administradores)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    // Actualización parcial de stock (Solo Administradores)
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(
            @PathVariable Long id, 
            @Valid @RequestBody StockUpdateDTO dto) {
        return ResponseEntity.ok(productoService.modificarStock(id, dto.cantidad()));
    }

    // Borrado lógico / Desactivar (Solo Administradores)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}