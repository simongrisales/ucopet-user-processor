package com.uco.ucopetuserprocessor.messaging.notification;

import com.uco.ucopetuserprocessor.domain.notification.NotificationDomain;
import com.uco.ucopetuserprocessor.service.notification.NotificationService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreationReceiverMessage {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificationCreationReceiverMessage.class);

    private final NotificationService notificationService;
    private final MapperJsonObject mapperJsonObject;

    public NotificationCreationReceiverMessage(NotificationService notificationService,
                                               MapperJsonObject mapperJsonObject) {
        this.notificationService = notificationService;
        this.mapperJsonObject = mapperJsonObject;
    }

    @RabbitListener(queues = "${notification.create.queue}")
    public void receiveMessageCreateNotification(String message) {
        try {
            NotificationDomain notification = mapperJsonObject.fromJson(message, NotificationDomain.class);
            notificationService.create(notification);
        } catch (Exception e) {
            LOGGER.error("No se pudo crear la notificación", e);
        }
    }
}