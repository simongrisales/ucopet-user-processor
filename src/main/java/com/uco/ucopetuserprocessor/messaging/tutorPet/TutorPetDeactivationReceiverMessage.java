package com.uco.ucopetuserprocessor.messaging.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import com.uco.ucopetuserprocessor.service.tutorPet.TutorPetService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TutorPetDeactivationReceiverMessage {

    private final MapperJsonObject mapperJsonObject;
    private final TutorPetService tutorPetService;
    private final TutorPetFailureHandler failureHandler;

    public TutorPetDeactivationReceiverMessage(
            MapperJsonObject mapperJsonObject,
            TutorPetService tutorPetService,
            TutorPetFailureHandler failureHandler) {
        this.mapperJsonObject = mapperJsonObject;
        this.tutorPetService = tutorPetService;
        this.failureHandler = failureHandler;
    }

    @RabbitListener(
            queues = "${user.tutor-pet.deactivation.queue-name}",
            containerFactory = "tutorPetListenerContainerFactory")
    public void listenDeactivate(String message) {
        try {
            TutorPetDomain tutorPet = mapperJsonObject.fromJson(message, TutorPetDomain.class);
            tutorPetService.deactivate(tutorPet == null ? null : tutorPet.getId());
        } catch (Exception e) {
            failureHandler.deactivation(message, e);
        }
    }
}