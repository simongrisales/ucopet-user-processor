package com.uco.ucopetuserprocessor.config.tutorPet;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TutorPetRabbitConfig {

    @Value("${user.tutor-pet.creation.exchange-name}")
    private String creationExchangeName;
    @Value("${user.tutor-pet.creation.queue-name}")
    private String creationQueueName;
    @Value("${user.tutor-pet.creation.routing-key-name}")
    private String creationRoutingKey;

    @Value("${user.tutor-pet.update.exchange-name}")
    private String updateExchangeName;
    @Value("${user.tutor-pet.update.queue-name}")
    private String updateQueueName;
    @Value("${user.tutor-pet.update.routing-key-name}")
    private String updateRoutingKey;

    @Value("${user.tutor-pet.deactivation.exchange-name}")
    private String deactivationExchangeName;
    @Value("${user.tutor-pet.deactivation.queue-name}")
    private String deactivationQueueName;
    @Value("${user.tutor-pet.deactivation.routing-key-name}")
    private String deactivationRoutingKey;

    @Value("${user.tutor-pet.dead-letter.exchange-name}")
    private String deadLetterExchangeName;

    @Value("${user.tutor-pet.creation.dead-letter-queue-name}")
    private String creationDeadLetterQueueName;
    @Value("${user.tutor-pet.creation.dead-letter-routing-key-name}")
    private String creationDeadLetterRoutingKey;
    @Value("${user.tutor-pet.update.dead-letter-queue-name}")
    private String updateDeadLetterQueueName;
    @Value("${user.tutor-pet.update.dead-letter-routing-key-name}")
    private String updateDeadLetterRoutingKey;
    @Value("${user.tutor-pet.deactivation.dead-letter-queue-name}")
    private String deactivationDeadLetterQueueName;
    @Value("${user.tutor-pet.deactivation.dead-letter-routing-key-name}")
    private String deactivationDeadLetterRoutingKey;

    @Bean
    DirectExchange tutorPetCreationExchange() {
        return new DirectExchange(creationExchangeName, true, false);
    }

    @Bean
    Queue tutorPetCreationQueue() {
        return new Queue(creationQueueName, true);
    }

    @Bean
    Binding tutorPetCreationBinding() {
        return BindingBuilder.bind(tutorPetCreationQueue())
                .to(tutorPetCreationExchange()).with(creationRoutingKey);
    }

    @Bean
    Queue tutorPetCreationDeadLetterQueue() {
        return new Queue(creationDeadLetterQueueName, true);
    }

    @Bean
    Binding tutorPetCreationDeadLetterBinding() {
        return BindingBuilder.bind(tutorPetCreationDeadLetterQueue())
                .to(tutorPetDeadLetterExchange()).with(creationDeadLetterRoutingKey);
    }

    @Bean
    DirectExchange tutorPetUpdateExchange() {
        return new DirectExchange(updateExchangeName, true, false);
    }

    @Bean
    Queue tutorPetUpdateQueue() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    Binding tutorPetUpdateBinding() {
        return BindingBuilder.bind(tutorPetUpdateQueue())
                .to(tutorPetUpdateExchange()).with(updateRoutingKey);
    }

    @Bean
    Queue tutorPetUpdateDeadLetterQueue() {
        return new Queue(updateDeadLetterQueueName, true);
    }

    @Bean
    Binding tutorPetUpdateDeadLetterBinding() {
        return BindingBuilder.bind(tutorPetUpdateDeadLetterQueue())
                .to(tutorPetDeadLetterExchange()).with(updateDeadLetterRoutingKey);
    }

    @Bean
    DirectExchange tutorPetDeactivationExchange() {
        return new DirectExchange(deactivationExchangeName, true, false);
    }

    @Bean
    Queue tutorPetDeactivationQueue() {
        return new Queue(deactivationQueueName, true);
    }

    @Bean
    Binding tutorPetDeactivationBinding() {
        return BindingBuilder.bind(tutorPetDeactivationQueue())
                .to(tutorPetDeactivationExchange()).with(deactivationRoutingKey);
    }

    @Bean
    Queue tutorPetDeactivationDeadLetterQueue() {
        return new Queue(deactivationDeadLetterQueueName, true);
    }

    @Bean
    Binding tutorPetDeactivationDeadLetterBinding() {
        return BindingBuilder.bind(tutorPetDeactivationDeadLetterQueue())
                .to(tutorPetDeadLetterExchange()).with(deactivationDeadLetterRoutingKey);
    }

    @Bean
    DirectExchange tutorPetDeadLetterExchange() {
        return new DirectExchange(deadLetterExchangeName, true, false);
    }

    @Bean(name = "tutorPetListenerContainerFactory")
    SimpleRabbitListenerContainerFactory tutorPetListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}