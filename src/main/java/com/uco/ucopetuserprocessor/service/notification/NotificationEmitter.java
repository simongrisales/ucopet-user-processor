package com.uco.ucopetuserprocessor.service.notification;

import com.uco.ucopetuserprocessor.domain.notification.NotificationDomain;
import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.model.notification.enums.NotificationType;
import com.uco.ucopetuserprocessor.repository.person.PersonRepository;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class NotificationEmitter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEmitter.class);
    private static final ZoneId ZONE_ID = ZoneId.of("America/Bogota");

    private final RabbitTemplate rabbitTemplate;
    private final MapperJsonObject mapperJsonObject;
    private final PersonRepository personRepository;

    @Value("${notification.create.exchange}")
    private String createExchange;

    @Value("${notification.create.routingkey}")
    private String createRoutingKey;

    public NotificationEmitter(RabbitTemplate rabbitTemplate,
                               MapperJsonObject mapperJsonObject,
                               PersonRepository personRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapperJsonObject = mapperJsonObject;
        this.personRepository = personRepository;
    }

    /**
     * Envía una notificación a cada administrador activo.
     * Si algo falla, solo se registra el error: nunca debe romper
     * la creación/edición/eliminación original.
     */
    public void emit(NotificationType type, String title, String message) {
        try {
            for (PersonDomain admin : personRepository.findByAdminTrueAndActiveTrue()) {
                NotificationDomain notification = new NotificationDomain();
                notification.setId(UUID.randomUUID());
                notification.setPersonId(admin.getId());
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setRead(false);
                notification.setType(type);
                notification.setCreatedAt(LocalDateTime.now(ZONE_ID));

                String json = mapperJsonObject.toJson(notification);
                rabbitTemplate.convertAndSend(createExchange, createRoutingKey, json);
            }
        } catch (Exception e) {
            LOGGER.error("No se pudo enviar la notificación {}", type, e);
        }
    }
}