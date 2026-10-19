package com.uco.ucopetuserprocessor.messaging.pet;

import com.uco.ucopetuserprocessor.domain.pet.PetDomain;
import com.uco.ucopetuserprocessor.service.pet.PetService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PetDeactivationReceiverMessage {

    private final MapperJsonObject mapperJsonObject;
    private final PetService petService;

    public PetDeactivationReceiverMessage(MapperJsonObject mapperJsonObject, PetService petService) {
        this.mapperJsonObject = mapperJsonObject;
        this.petService = petService;
    }

    @RabbitListener(queues = "${user.pet.deactivation.queue-name}")
    public void receiveMessageUpdatePet(String message) {
        try {
            PetDomain pet = mapperJsonObject.fromJson(message, PetDomain.class);

            petService.deactivatePet(pet.getId());

        } catch (Exception e) {
            System.err.println(
                    "Error al recibir la actualización de Pet: "
                            + e.getMessage()
            );
            throw e;
        }
    }
}
