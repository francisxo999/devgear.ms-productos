package com.devgear.ms_productos.repository;

import com.devgear.ms_productos.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Obtener solo los productos activos con paginación (para el catálogo del cliente)
    Page<Producto> findByActivoTrue(Pageable pageable);

    // Filtrar por categoría y que estén activos
    Page<Producto> findByCategoriaAndActivoTrue(String categoria, Pageable pageable);

    // Buscar por ID solo si está activo
    Optional<Producto> findByIdAndActivoTrue(Long id);
}