package com.uco.ucopetuserprocessor.messaging.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.service.tutorPet.TutorPetService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TutorPetUpdateReceiverMessage {

    private final MapperJsonObject mapperJsonObject;
    private final TutorPetService tutorPetService;

    public TutorPetUpdateReceiverMessage(MapperJsonObject mapperJsonObject, TutorPetService tutorPetService) {
        this.mapperJsonObject = mapperJsonObject;
        this.tutorPetService = tutorPetService;
    }

    @RabbitListener(queues = "${user.tutor-pet.update.queue-name}")
    public void listenUpdate(String message) {
        TutorPetDomain tutorPet;
        try {
            tutorPet = mapperJsonObject.fromJson(message, TutorPetDomain.class);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("El mensaje de actualización de TutorPet tiene un formato inválido.");
        }
        tutorPetService.update(tutorPet == null ? null : tutorPet.getId(), tutorPet);
    }
}