package com.devgear.ms_productos.kafka;

/**
 * Evento publicado en el tópico "navegacion.eventos".
 * tipo: PRODUCTO_VISTO | ITEM_AGREGADO | ITEM_ELIMINADO
 * La clave del mensaje Kafka es usuarioId, así todos los eventos de un usuario
 * caen en la misma partición y conservan su orden.
 */
public record NavegacionEvento(
        String eventId,
        String tipo,
        String usuarioId,
        Long productoId,
        String categoria,
        Integer cantidad,
        String timestamp
) {}
