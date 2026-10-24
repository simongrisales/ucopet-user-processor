package com.uco.ucopetuserprocessor.messaging.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.service.tutorPet.TutorPetService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TutorPetDeactivationReceiverMessage {

    private final MapperJsonObject mapperJsonObject;
    private final TutorPetService tutorPetService;

    public TutorPetDeactivationReceiverMessage(MapperJsonObject mapperJsonObject, TutorPetService tutorPetService) {
        this.mapperJsonObject = mapperJsonObject;
        this.tutorPetService = tutorPetService;
    }

    @RabbitListener(queues = "${user.tutor-pet.deactivation.queue-name}")
    public void listenDeactivate(String message) {
        TutorPetDomain tutorPet;
        try {
            tutorPet = mapperJsonObject.fromJson(message, TutorPetDomain.class);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("El mensaje de desactivación de TutorPet tiene un formato inválido.");
        }
        tutorPetService.deactivate(tutorPet == null ? null : tutorPet.getId());
    }
}