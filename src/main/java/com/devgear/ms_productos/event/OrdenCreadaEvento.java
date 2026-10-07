package com.devgear.ms_productos.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Misma forma que el evento publicado por ms-ordenes. No se comparte código
// entre microservicios: si el contrato cambia allá, hay que replicarlo acá.
public record OrdenCreadaEvento(
    Long ordenId,
    String usuarioId,
    String email,
    LocalDateTime fecha,
    BigDecimal total,
    List<ItemEvento> items
) {
    public record ItemEvento(
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario
    ) {}
}