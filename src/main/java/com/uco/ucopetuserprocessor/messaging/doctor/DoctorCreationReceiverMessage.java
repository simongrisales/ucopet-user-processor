package com.uco.ucopetuserprocessor.messaging.doctor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DoctorCreationReceiverMessage {

	@RabbitListener(queues = "${ucopet.rabbitmq.user.doctor.create.queue}")
	public void listenCreate(String message) {
		// TODO: Process doctor creation message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.doctor.update.queue}")
	public void listenUpdate(String message) {
		// TODO: Process doctor update message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.doctor.deactivate.queue}")
	public void listenDeactivate(String message) {
		// TODO: Process doctor deactivation message.
	}
}