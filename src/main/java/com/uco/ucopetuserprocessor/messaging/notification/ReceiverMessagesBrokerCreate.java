package com.uco.ucopetuserprocessor.messaging.notification;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ReceiverMessagesBrokerCreate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverMessagesBrokerCreate.class);

    private final NotificationService notificationService;
    private final MapperJsonObjeto mapperJsonObjeto;

    public ReceiverMessagesBrokerCreate(NotificationService notificationService,
                                        MapperJsonObjeto mapperJsonObjeto) {
        this.notificationService = notificationService;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

    @RabbitListener(queues = "${notification.queue-recibir.create.queue-name}")
    public void receiveMessageCreateNotification(String message) {
        try {
            notificationService.save(obtenerNotificacion(message).get());
        } catch (Exception e) {
            LOGGER.error("Error procesando mensaje de creación de notificación: {}", message, e);
        }
    }

    private Optional<Notification> obtenerNotificacion(String mensaje) {
        return mapperJsonObjeto.ejecutar(mensaje, Notification.class);
    }
}
