package com.devgear.ms_productos.dto;

import jakarta.validation.constraints.NotNull;

public record StockUpdateDTO(
    @NotNull(message = "La cantidad es obligatoria")
    Integer cantidad
) {}