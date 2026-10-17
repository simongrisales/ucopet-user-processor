package com.uco.ucopetuserprocessor.messaging.person;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PersonCreationReceiverMessage {

	@RabbitListener(queues = "${ucopet.rabbitmq.user.person.create.queue}")
	public void listenCreate(String message) {
		// TODO: Process person creation message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.person.update.queue}")
	public void listenUpdate(String message) {
		// TODO: Process person update message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.person.deactivate.queue}")
	public void listenDeactivate(String message) {
		// TODO: Process person deactivation message.
	}
}