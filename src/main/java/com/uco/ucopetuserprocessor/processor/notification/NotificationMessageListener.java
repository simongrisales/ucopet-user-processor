package com.uco.ucopetuserprocessor.processor.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageListener {

	@RabbitListener(queues = "${ucopet.rabbitmq.user.notification.create.queue}")
	public void listenCreate(String message) {
		// TODO: Process notification creation message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.notification.update.queue}")
	public void listenUpdate(String message) {
		// TODO: Process notification update message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.notification.deactivate.queue}")
	public void listenDeactivate(String message) {
		// TODO: Process notification deactivation message.
	}
}