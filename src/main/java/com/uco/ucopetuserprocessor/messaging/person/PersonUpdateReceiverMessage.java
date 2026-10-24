package com.uco.ucopetuserprocessor.messaging.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.service.person.PersonService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;

@Component
public class PersonUpdateReceiverMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonUpdateReceiverMessage.class);

    private final MapperJsonObject mapperJsonObject;
    private final PersonService personService;

    public PersonUpdateReceiverMessage(MapperJsonObject mapperJsonObject, PersonService personService) {
        this.mapperJsonObject = mapperJsonObject;
        this.personService = personService;
    }

    @RabbitListener(queues = "${user.person.update.queue-name}")
    public void receiveMessageUpdatePerson(String message) {
        PersonDomain person;
        try {
            person = mapperJsonObject.fromJson(message, PersonDomain.class);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Mensaje para actualizar una persona descartado: el JSON no es valido", e);
            return;
        }
        if (person == null) {
            LOGGER.error("Mensaje para actualizar una persona descartado: llego vacio");
            return;
        }

        try {
            personService.update(person);
            LOGGER.info("Persona {}: se pudo actualizar", person.getId());
        } catch (BusinessException e) {
            LOGGER.warn("No se pudo actualizar la persona {}: {}", person.getId(), e.getMessage());
        }
    }
}