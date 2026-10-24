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
public class PersonDeactivationReceiverMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDeactivationReceiverMessage.class);

    private final MapperJsonObject mapperJsonObject;
    private final PersonService personService;

    public PersonDeactivationReceiverMessage(MapperJsonObject mapperJsonObject, PersonService personService) {
        this.mapperJsonObject = mapperJsonObject;
        this.personService = personService;
    }

    @RabbitListener(queues = "${user.person.deactivation.queue-name}")
    public void receiveMessageDeactivatePerson(String message) {
        PersonDomain person;
        try {
            person = mapperJsonObject.fromJson(message, PersonDomain.class);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Mensaje para dar de baja una persona descartado: el JSON no es valido", e);
            return;
        }
        if (person == null) {
            LOGGER.error("Mensaje para dar de baja una persona descartado: llego vacio");
            return;
        }

        try {
            personService.deactivate(person.getId());
            LOGGER.info("Persona {}: se pudo dar de baja", person.getId());
        } catch (BusinessException e) {
            LOGGER.warn("No se pudo dar de baja la persona {}: {}", person.getId(), e.getMessage());
        }
    }
}