package com.uco.ucopetuserprocessor.messaging.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.uco.ucopetuserprocessor.dto.person.PersonMessageDTO;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.service.person.PersonService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;

@Component
public class PersonCreationReceiverMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCreationReceiverMessage.class);

    private final MapperJsonObject mapperJsonObject;
    private final PersonService personService;

    public PersonCreationReceiverMessage(MapperJsonObject mapperJsonObject, PersonService personService) {
        this.mapperJsonObject = mapperJsonObject;
        this.personService = personService;
    }

    @RabbitListener(queues = "${user.person.creation.queue-name}")
    public void receiveMessageCreatePerson(String message) {
        PersonMessageDTO person;
        try {
            person = mapperJsonObject.fromJson(message, PersonMessageDTO.class);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Mensaje para crear una persona descartado: el JSON no es valido", e);
            return;
        }
        if (person == null) {
            LOGGER.error("Mensaje para crear una persona descartado: llego vacio");
            return;
        }

        try {
            personService.create(person);
            LOGGER.info("Persona {}: se pudo crear", person.id());
        } catch (BusinessException e) {
            LOGGER.warn("No se pudo crear la persona {}: {}", person.id(), e.getMessage());
        }
    }
}