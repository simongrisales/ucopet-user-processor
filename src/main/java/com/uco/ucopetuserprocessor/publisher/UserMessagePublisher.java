package com.uco.ucopetuserprocessor.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserMessagePublisher {

	private final RabbitTemplate rabbitTemplate;
	private final String userExchangeName;
	private final String personCreateRoutingKey;
	private final String personUpdateRoutingKey;
	private final String personDeactivateRoutingKey;
	private final String tutorPetCreateRoutingKey;
	private final String tutorPetUpdateRoutingKey;
	private final String tutorPetDeactivateRoutingKey;
	private final String petCreateRoutingKey;
	private final String petUpdateRoutingKey;
	private final String petDeactivateRoutingKey;
	private final String doctorCreateRoutingKey;
	private final String doctorUpdateRoutingKey;
	private final String doctorDeactivateRoutingKey;
	private final String notificationCreateRoutingKey;
	private final String notificationUpdateRoutingKey;
	private final String notificationDeactivateRoutingKey;

	public UserMessagePublisher(
			RabbitTemplate rabbitTemplate,
			@Value("${ucopet.rabbitmq.user.exchange}") String userExchangeName,
			@Value("${ucopet.rabbitmq.user.person.create.routing-key}") String personCreateRoutingKey,
			@Value("${ucopet.rabbitmq.user.person.update.routing-key}") String personUpdateRoutingKey,
			@Value("${ucopet.rabbitmq.user.person.deactivate.routing-key}") String personDeactivateRoutingKey,
			@Value("${ucopet.rabbitmq.user.tutor-pet.create.routing-key}") String tutorPetCreateRoutingKey,
			@Value("${ucopet.rabbitmq.user.tutor-pet.update.routing-key}") String tutorPetUpdateRoutingKey,
			@Value("${ucopet.rabbitmq.user.tutor-pet.deactivate.routing-key}") String tutorPetDeactivateRoutingKey,
			@Value("${ucopet.rabbitmq.user.pet.create.routing-key}") String petCreateRoutingKey,
			@Value("${ucopet.rabbitmq.user.pet.update.routing-key}") String petUpdateRoutingKey,
			@Value("${ucopet.rabbitmq.user.pet.deactivate.routing-key}") String petDeactivateRoutingKey,
			@Value("${ucopet.rabbitmq.user.doctor.create.routing-key}") String doctorCreateRoutingKey,
			@Value("${ucopet.rabbitmq.user.doctor.update.routing-key}") String doctorUpdateRoutingKey,
			@Value("${ucopet.rabbitmq.user.doctor.deactivate.routing-key}") String doctorDeactivateRoutingKey,
			@Value("${ucopet.rabbitmq.user.notification.create.routing-key}") String notificationCreateRoutingKey,
			@Value("${ucopet.rabbitmq.user.notification.update.routing-key}") String notificationUpdateRoutingKey,
			@Value("${ucopet.rabbitmq.user.notification.deactivate.routing-key}") String notificationDeactivateRoutingKey) {
		this.rabbitTemplate = rabbitTemplate;
		this.userExchangeName = userExchangeName;
		this.personCreateRoutingKey = personCreateRoutingKey;
		this.personUpdateRoutingKey = personUpdateRoutingKey;
		this.personDeactivateRoutingKey = personDeactivateRoutingKey;
		this.tutorPetCreateRoutingKey = tutorPetCreateRoutingKey;
		this.tutorPetUpdateRoutingKey = tutorPetUpdateRoutingKey;
		this.tutorPetDeactivateRoutingKey = tutorPetDeactivateRoutingKey;
		this.petCreateRoutingKey = petCreateRoutingKey;
		this.petUpdateRoutingKey = petUpdateRoutingKey;
		this.petDeactivateRoutingKey = petDeactivateRoutingKey;
		this.doctorCreateRoutingKey = doctorCreateRoutingKey;
		this.doctorUpdateRoutingKey = doctorUpdateRoutingKey;
		this.doctorDeactivateRoutingKey = doctorDeactivateRoutingKey;
		this.notificationCreateRoutingKey = notificationCreateRoutingKey;
		this.notificationUpdateRoutingKey = notificationUpdateRoutingKey;
		this.notificationDeactivateRoutingKey = notificationDeactivateRoutingKey;
	}

	public void publishPersonCreate(Object message) {
		publish(personCreateRoutingKey, message);
	}

	public void publishPersonUpdate(Object message) {
		publish(personUpdateRoutingKey, message);
	}

	public void publishPersonDeactivate(Object message) {
		publish(personDeactivateRoutingKey, message);
	}

	public void publishTutorPetCreate(Object message) {
		publish(tutorPetCreateRoutingKey, message);
	}

	public void publishTutorPetUpdate(Object message) {
		publish(tutorPetUpdateRoutingKey, message);
	}

	public void publishTutorPetDeactivate(Object message) {
		publish(tutorPetDeactivateRoutingKey, message);
	}

	public void publishPetCreate(Object message) {
		publish(petCreateRoutingKey, message);
	}

	public void publishPetUpdate(Object message) {
		publish(petUpdateRoutingKey, message);
	}

	public void publishPetDeactivate(Object message) {
		publish(petDeactivateRoutingKey, message);
	}

	public void publishDoctorCreate(Object message) {
		publish(doctorCreateRoutingKey, message);
	}

	public void publishDoctorUpdate(Object message) {
		publish(doctorUpdateRoutingKey, message);
	}

	public void publishDoctorDeactivate(Object message) {
		publish(doctorDeactivateRoutingKey, message);
	}

	public void publishNotificationCreate(Object message) {
		publish(notificationCreateRoutingKey, message);
	}

	public void publishNotificationUpdate(Object message) {
		publish(notificationUpdateRoutingKey, message);
	}

	public void publishNotificationDeactivate(Object message) {
		publish(notificationDeactivateRoutingKey, message);
	}

	private void publish(String routingKey, Object message) {
		rabbitTemplate.convertAndSend(userExchangeName, routingKey, message);
	}
}