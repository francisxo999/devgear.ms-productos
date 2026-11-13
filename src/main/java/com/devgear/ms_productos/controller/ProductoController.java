package com.devgear.ms_productos.controller;

import com.devgear.ms_productos.dto.ProductoRequestDTO;
import com.devgear.ms_productos.dto.ProductoResponseDTO;
import com.devgear.ms_productos.dto.StockUpdateDTO;
import com.devgear.ms_productos.kafka.NavegacionProducer;
import com.devgear.ms_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final NavegacionProducer navegacionProducer;

    public ProductoController(ProductoService productoService, NavegacionProducer navegacionProducer) {
        this.productoService = productoService;
        this.navegacionProducer = navegacionProducer;
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductos(
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // El frontend lo llama cuando el usuario abre/mira un producto.
    // Publica PRODUCTO_VISTO en Kafka (tópico navegacion.eventos, clave = usuario).
    // Es un endpoint aparte de GET /{id} porque ms-carrito también consulta GET /{id}
    // para validar stock, y eso no es "mirar" un producto.
    @PostMapping("/{id}/vista")
    public ResponseEntity<Void> registrarVista(@PathVariable Long id, JwtAuthenticationToken auth) {
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        String usuarioId = auth.getToken().getClaimAsString("oid");
        navegacionProducer.publicar("PRODUCTO_VISTO", usuarioId, producto.id(), producto.categoria(), null);
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(
            @PathVariable Long id, 
            @Valid @RequestBody StockUpdateDTO dto) {
        return ResponseEntity.ok(productoService.modificarStock(id, dto.cantidad()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
