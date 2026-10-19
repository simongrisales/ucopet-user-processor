package com.uco.ucopetuserprocessor.messaging.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class ReceiverMessagesBrokerDelete {

    @Autowired
    private NotificationService notificationService;

    private final MapperJsonObjeto mapperJsonObjeto;

    public ReceiverMessagesBrokerDelete(MapperJsonObjeto mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "${notification.queue-recibir.delete.queue-name}")
    public void receiveMessageProcessNotification(String message) {
        try {
            notificationService.delete(obtenerObjetoDeMensaje(message).get());
        } catch (Exception e) {
            System.out.println(e);
        }

}
