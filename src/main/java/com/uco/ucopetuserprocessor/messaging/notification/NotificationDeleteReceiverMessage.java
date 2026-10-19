package com.uco.ucopetuserprocessor.messaging.notification;

import com.uco.ucopetuserprocessor.domain.notification.NotificationDomain;
import com.uco.ucopetuserprocessor.service.notification.NotificationService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObjectJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationDeleteReceiverMessage {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificationDeleteReceiverMessage.class);
    private final NotificationService notificationService;
    private final MapperJsonObjectJackson mapperJsonObjet;
    public NotificationDeleteReceiverMessage(NotificationService notificationService,
                                             MapperJsonObjectJackson mapperJsonObjet) {
        this.notificationService = notificationService;
        this.mapperJsonObjet = mapperJsonObjet;
    }
    @RabbitListener(queues = "${notification.delete.queue}")
    public void receiveMessageProcessNotification(String message) {
        try {
            NotificationDomain notification = mapperJsonObjet.fromJson(message, NotificationDomain.class);
            notificationService.delete(notification.getId());
        } catch (Exception e) {
            LOGGER.error("No se pudo eliminar la notificación", e);
        }
    }
}


