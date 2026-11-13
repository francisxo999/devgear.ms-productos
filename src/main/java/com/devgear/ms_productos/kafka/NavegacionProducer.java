package com.devgear.ms_productos.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

/**
 * Publica eventos de navegación sin bloquear ni romper la petición HTTP:
 * si Kafka no responde, el error se registra y la compra/visita sigue normal.
 */
@Component
public class NavegacionProducer {

    private static final Logger log = LoggerFactory.getLogger(NavegacionProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;
    private final String topic;

    public NavegacionProducer(KafkaTemplate<String, String> kafkaTemplate,
                              JsonMapper jsonMapper,
                              @Value("${devgear.kafka.topic-navegacion:navegacion.eventos}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
        this.topic = topic;
    }

    public void publicar(String tipo, String usuarioId, Long productoId, String categoria, Integer cantidad) {
        try {
            NavegacionEvento evento = new NavegacionEvento(
                    UUID.randomUUID().toString(), tipo, usuarioId, productoId, categoria, cantidad,
                    Instant.now().toString());
            String json = jsonMapper.writeValueAsString(evento);
            kafkaTemplate.send(topic, usuarioId, json).whenComplete((res, ex) -> {
                if (ex != null) {
                    log.warn("No se pudo publicar {} en {}: {}", tipo, topic, ex.getMessage());
                } else {
                    log.debug("{} -> {}-{}@{}", tipo, topic,
                            res.getRecordMetadata().partition(), res.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.warn("No se pudo publicar {} en {}: {}", tipo, topic, e.getMessage());
        }
    }
}
