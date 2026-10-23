package com.uco.ucopetuserprocessor.messaging.tutorPet;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TutorPetCreationReceiverMessage {

	// Descomentar cuando tenga las colas, ya que interferia con el API
	/*@RabbitListener(queues = "${ucopet.rabbitmq.user.tutor-pet.create.queue}")
	public void listenCreate(String message) {
		// TODO: Process tutor pet creation message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.tutor-pet.update.queue}")
	public void listenUpdate(String message) {
		// TODO: Process tutor pet update message.
	}

	@RabbitListener(queues = "${ucopet.rabbitmq.user.tutor-pet.deactivate.queue}")
	public void listenDeactivate(String message) {
		// TODO: Process tutor pet deactivation message.
	}
*/}