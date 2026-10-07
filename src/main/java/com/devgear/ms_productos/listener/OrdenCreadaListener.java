package com.devgear.ms_productos.listener;

import com.devgear.ms_productos.config.RabbitMQConfig;
import com.devgear.ms_productos.event.OrdenCreadaEvento;
import com.devgear.ms_productos.service.ProductoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private final ProductoService productoService;

    public OrdenCreadaListener(ProductoService productoService) {
        this.productoService = productoService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PRODUCTOS)
    public void onOrdenCreada(OrdenCreadaEvento evento) {
        for (OrdenCreadaEvento.ItemEvento item : evento.items()) {
            try {
                productoService.modificarStock(item.productoId(), -item.cantidad());
            } catch (Exception ex) {
                System.err.println("No se pudo descontar stock del producto " + item.productoId()
                        + " (orden " + evento.ordenId() + "): " + ex.getMessage());
            }
        }
    }
}