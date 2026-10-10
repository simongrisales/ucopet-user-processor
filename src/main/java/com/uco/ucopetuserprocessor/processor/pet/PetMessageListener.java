package com.uco.ucopetuserprocessor.processor.pet;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PetMessageListener {

	@RabbitListener(queues = "${ucopet.rabbitmq.user.pet.create.queue}")
	public void listenCreate(String message) {
		// TODO: Process pet creation message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.pet.update.queue}")
	public void listenUpdate(String message) {
		// TODO: Process pet update message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.pet.deactivate.queue}")
	public void listenDeactivate(String message) {
		// TODO: Process pet deactivation message.
	}
}