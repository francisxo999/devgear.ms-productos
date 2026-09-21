package com.devgear.ms_productos.dto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String categoria,
    String imagenUrl,
    Boolean activo
) {}