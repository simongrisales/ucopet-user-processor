package com.uco.ucopetuserprocessor.config.person;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonRabbitConfig {

    @Value("${user.person.creation.exchange-name}") private String creationEx;
    @Value("${user.person.creation.queue-name}") private String creationQu;
    @Value("${user.person.creation.routing-key-name}") private String creationRk;
    @Value("${user.person.update.exchange-name}") private String updateEx;
    @Value("${user.person.update.queue-name}") private String updateQu;
    @Value("${user.person.update.routing-key-name}") private String updateRk;
    @Value("${user.person.deactivation.exchange-name}") private String deactivationEx;
    @Value("${user.person.deactivation.queue-name}") private String deactivationQu;
    @Value("${user.person.deactivation.routing-key-name}") private String deactivationRk;

    @Bean
    DirectExchange personCreationExchange() {
        return new DirectExchange(creationEx, true, false);
    }
    @Bean
    Queue personCreationQueue() {
        return new Queue(creationQu, true);
    }
    @Bean
    Binding personCreationBinding() {
        return BindingBuilder.bind(personCreationQueue())
                .to(personCreationExchange()).with(creationRk);
    }

    @Bean
    DirectExchange personUpdateExchange() {
        return new DirectExchange(updateEx, true, false);
    }
    @Bean
    Queue personUpdateQueue() {
        return new Queue(updateQu, true);
    }
    @Bean
    Binding personUpdateBinding() {
        return BindingBuilder.bind(personUpdateQueue())
                .to(personUpdateExchange()).with(updateRk);
    }

    @Bean
    DirectExchange personDeactivationExchange() {
        return new DirectExchange(deactivationEx, true, false);
    }
    @Bean
    Queue personDeactivationQueue() {
        return new Queue(deactivationQu, true);
    }
    @Bean
    Binding personDeactivationBinding() {
        return BindingBuilder.bind(personDeactivationQueue())
                .to(personDeactivationExchange()).with(deactivationRk);
    }
}