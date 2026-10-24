package com.uco.ucopetuserprocessor.messaging.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
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
        TutorPetDomain tutorPet = mapperJsonObject.fromJson(message, TutorPetDomain.class);
        tutorPetService.update(tutorPet.getId(), tutorPet);
    }
}