package com.devgear.ms_productos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Mismo nombre que declaró ms-ordenes.
    public static final String EXCHANGE_ORDEN_CREADA = "orden.creada.exchange";

    public static final String QUEUE_PRODUCTOS = "productos.orden-creada.queue";

    @Bean
    public FanoutExchange ordenCreadaExchange() {
        return new FanoutExchange(EXCHANGE_ORDEN_CREADA);
    }

    @Bean
    public Queue productosQueue() {
        return new Queue(QUEUE_PRODUCTOS, true); // durable=true: sobrevive un reinicio de RabbitMQ
    }

    @Bean
    public Binding productosBinding(Queue productosQueue, FanoutExchange ordenCreadaExchange) {
        return BindingBuilder.bind(productosQueue).to(ordenCreadaExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}