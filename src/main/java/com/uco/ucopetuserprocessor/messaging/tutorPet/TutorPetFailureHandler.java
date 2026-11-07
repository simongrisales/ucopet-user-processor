package com.uco.ucopetuserprocessor.messaging.tutorPet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TutorPetFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TutorPetFailureHandler.class);

    private final RabbitTemplate rabbitTemplate;
    private final String deadLetterExchange;
    private final String creationRoutingKey;
    private final String updateRoutingKey;
    private final String deactivationRoutingKey;

    public TutorPetFailureHandler(
            RabbitTemplate rabbitTemplate,
            @Value("${user.tutor-pet.dead-letter.exchange-name}") String deadLetterExchange,
            @Value("${user.tutor-pet.creation.dead-letter-routing-key-name}") String creationRoutingKey,
            @Value("${user.tutor-pet.update.dead-letter-routing-key-name}") String updateRoutingKey,
            @Value("${user.tutor-pet.deactivation.dead-letter-routing-key-name}") String deactivationRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.deadLetterExchange = deadLetterExchange;
        this.creationRoutingKey = creationRoutingKey;
        this.updateRoutingKey = updateRoutingKey;
        this.deactivationRoutingKey = deactivationRoutingKey;
    }

    public void creation(String message, Exception error) {
        sendToDeadLetter("creation", creationRoutingKey, message, error);
    }

    public void update(String message, Exception error) {
        sendToDeadLetter("update", updateRoutingKey, message, error);
    }

    public void deactivation(String message, Exception error) {
        sendToDeadLetter("deactivation", deactivationRoutingKey, message, error);
    }

    private void sendToDeadLetter(String operation, String routingKey, String message, Exception error) {
        LOGGER.error("Falló el mensaje de TutorPet ({}) y se enviará a la DLQ", operation, error);
        rabbitTemplate.convertAndSend(deadLetterExchange, routingKey, message);
    }
}