package com.uco.ucopetuserprocessor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserQueueConfig {

	@Value("${ucopet.rabbitmq.user.exchange}")
	private String userExchangeName;

	@Value("${ucopet.rabbitmq.user.person.create.queue}")
	private String personCreateQueueName;

	@Value("${ucopet.rabbitmq.user.person.create.routing-key}")
	private String personCreateRoutingKey;

	@Value("${ucopet.rabbitmq.user.person.update.queue}")
	private String personUpdateQueueName;

	@Value("${ucopet.rabbitmq.user.person.update.routing-key}")
	private String personUpdateRoutingKey;

	@Value("${ucopet.rabbitmq.user.person.deactivate.queue}")
	private String personDeactivateQueueName;

	@Value("${ucopet.rabbitmq.user.person.deactivate.routing-key}")
	private String personDeactivateRoutingKey;

	@Value("${ucopet.rabbitmq.user.tutor-pet.create.queue}")
	private String tutorPetCreateQueueName;

	@Value("${ucopet.rabbitmq.user.tutor-pet.create.routing-key}")
	private String tutorPetCreateRoutingKey;

	@Value("${ucopet.rabbitmq.user.tutor-pet.update.queue}")
	private String tutorPetUpdateQueueName;

	@Value("${ucopet.rabbitmq.user.tutor-pet.update.routing-key}")
	private String tutorPetUpdateRoutingKey;

	@Value("${ucopet.rabbitmq.user.tutor-pet.deactivate.queue}")
	private String tutorPetDeactivateQueueName;

	@Value("${ucopet.rabbitmq.user.tutor-pet.deactivate.routing-key}")
	private String tutorPetDeactivateRoutingKey;

	@Value("${ucopet.rabbitmq.user.pet.create.queue}")
	private String petCreateQueueName;

	@Value("${ucopet.rabbitmq.user.pet.create.routing-key}")
	private String petCreateRoutingKey;

	@Value("${ucopet.rabbitmq.user.pet.update.queue}")
	private String petUpdateQueueName;

	@Value("${ucopet.rabbitmq.user.pet.update.routing-key}")
	private String petUpdateRoutingKey;

	@Value("${ucopet.rabbitmq.user.pet.deactivate.queue}")
	private String petDeactivateQueueName;

	@Value("${ucopet.rabbitmq.user.pet.deactivate.routing-key}")
	private String petDeactivateRoutingKey;

	@Value("${ucopet.rabbitmq.user.doctor.create.queue}")
	private String doctorCreateQueueName;

	@Value("${ucopet.rabbitmq.user.doctor.create.routing-key}")
	private String doctorCreateRoutingKey;

	@Value("${ucopet.rabbitmq.user.doctor.update.queue}")
	private String doctorUpdateQueueName;

	@Value("${ucopet.rabbitmq.user.doctor.update.routing-key}")
	private String doctorUpdateRoutingKey;

	@Value("${ucopet.rabbitmq.user.doctor.deactivate.queue}")
	private String doctorDeactivateQueueName;

	@Value("${ucopet.rabbitmq.user.doctor.deactivate.routing-key}")
	private String doctorDeactivateRoutingKey;

	@Value("${ucopet.rabbitmq.user.notification.create.queue}")
	private String notificationCreateQueueName;

	@Value("${ucopet.rabbitmq.user.notification.create.routing-key}")
	private String notificationCreateRoutingKey;

	@Value("${ucopet.rabbitmq.user.notification.update.queue}")
	private String notificationUpdateQueueName;

	@Value("${ucopet.rabbitmq.user.notification.update.routing-key}")
	private String notificationUpdateRoutingKey;

	@Value("${ucopet.rabbitmq.user.notification.deactivate.queue}")
	private String notificationDeactivateQueueName;

	@Value("${ucopet.rabbitmq.user.notification.deactivate.routing-key}")
	private String notificationDeactivateRoutingKey;

	@Bean
	public DirectExchange userExchange() {
		return new DirectExchange(userExchangeName);
	}

	@Bean
	public Queue personCreateQueue() {
		return new Queue(personCreateQueueName, true);
	}

	@Bean
	public Queue personUpdateQueue() {
		return new Queue(personUpdateQueueName, true);
	}

	@Bean
	public Queue personDeactivateQueue() {
		return new Queue(personDeactivateQueueName, true);
	}

	@Bean
	public Queue tutorPetCreateQueue() {
		return new Queue(tutorPetCreateQueueName, true);
	}

	@Bean
	public Queue tutorPetUpdateQueue() {
		return new Queue(tutorPetUpdateQueueName, true);
	}

	@Bean
	public Queue tutorPetDeactivateQueue() {
		return new Queue(tutorPetDeactivateQueueName, true);
	}

	@Bean
	public Queue petCreateQueue() {
		return new Queue(petCreateQueueName, true);
	}

	@Bean
	public Queue petUpdateQueue() {
		return new Queue(petUpdateQueueName, true);
	}

	@Bean
	public Queue petDeactivateQueue() {
		return new Queue(petDeactivateQueueName, true);
	}

	@Bean
	public Queue doctorCreateQueue() {
		return new Queue(doctorCreateQueueName, true);
	}

	@Bean
	public Queue doctorUpdateQueue() {
		return new Queue(doctorUpdateQueueName, true);
	}

	@Bean
	public Queue doctorDeactivateQueue() {
		return new Queue(doctorDeactivateQueueName, true);
	}

	@Bean
	public Queue notificationCreateQueue() {
		return new Queue(notificationCreateQueueName, true);
	}

	@Bean
	public Queue notificationUpdateQueue() {
		return new Queue(notificationUpdateQueueName, true);
	}

	@Bean
	public Queue notificationDeactivateQueue() {
		return new Queue(notificationDeactivateQueueName, true);
	}

	@Bean
	public Binding personCreateBinding(Queue personCreateQueue, DirectExchange userExchange) {
		return bind(personCreateQueue, userExchange, personCreateRoutingKey);
	}

	@Bean
	public Binding personUpdateBinding(Queue personUpdateQueue, DirectExchange userExchange) {
		return bind(personUpdateQueue, userExchange, personUpdateRoutingKey);
	}

	@Bean
	public Binding personDeactivateBinding(Queue personDeactivateQueue, DirectExchange userExchange) {
		return bind(personDeactivateQueue, userExchange, personDeactivateRoutingKey);
	}

	@Bean
	public Binding tutorPetCreateBinding(Queue tutorPetCreateQueue, DirectExchange userExchange) {
		return bind(tutorPetCreateQueue, userExchange, tutorPetCreateRoutingKey);
	}

	@Bean
	public Binding tutorPetUpdateBinding(Queue tutorPetUpdateQueue, DirectExchange userExchange) {
		return bind(tutorPetUpdateQueue, userExchange, tutorPetUpdateRoutingKey);
	}

	@Bean
	public Binding tutorPetDeactivateBinding(Queue tutorPetDeactivateQueue, DirectExchange userExchange) {
		return bind(tutorPetDeactivateQueue, userExchange, tutorPetDeactivateRoutingKey);
	}

	@Bean
	public Binding petCreateBinding(Queue petCreateQueue, DirectExchange userExchange) {
		return bind(petCreateQueue, userExchange, petCreateRoutingKey);
	}

	@Bean
	public Binding petUpdateBinding(Queue petUpdateQueue, DirectExchange userExchange) {
		return bind(petUpdateQueue, userExchange, petUpdateRoutingKey);
	}

	@Bean
	public Binding petDeactivateBinding(Queue petDeactivateQueue, DirectExchange userExchange) {
		return bind(petDeactivateQueue, userExchange, petDeactivateRoutingKey);
	}

	@Bean
	public Binding doctorCreateBinding(Queue doctorCreateQueue, DirectExchange userExchange) {
		return bind(doctorCreateQueue, userExchange, doctorCreateRoutingKey);
	}

	@Bean
	public Binding doctorUpdateBinding(Queue doctorUpdateQueue, DirectExchange userExchange) {
		return bind(doctorUpdateQueue, userExchange, doctorUpdateRoutingKey);
	}

	@Bean
	public Binding doctorDeactivateBinding(Queue doctorDeactivateQueue, DirectExchange userExchange) {
		return bind(doctorDeactivateQueue, userExchange, doctorDeactivateRoutingKey);
	}

	@Bean
	public Binding notificationCreateBinding(Queue notificationCreateQueue, DirectExchange userExchange) {
		return bind(notificationCreateQueue, userExchange, notificationCreateRoutingKey);
	}

	@Bean
	public Binding notificationUpdateBinding(Queue notificationUpdateQueue, DirectExchange userExchange) {
		return bind(notificationUpdateQueue, userExchange, notificationUpdateRoutingKey);
	}

	@Bean
	public Binding notificationDeactivateBinding(Queue notificationDeactivateQueue, DirectExchange userExchange) {
		return bind(notificationDeactivateQueue, userExchange, notificationDeactivateRoutingKey);
	}

	private Binding bind(Queue queue, DirectExchange exchange, String routingKey) {
		return BindingBuilder.bind(queue).to(exchange).with(routingKey);
	}
}