package com.uco.ucopetuserprocessor.messaging.pet;

import com.uco.ucopetuserprocessor.domain.pet.PetDomain;
import com.uco.ucopetuserprocessor.service.pet.PetService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PetCreationReceiverMessage {

	private final MapperJsonObject mapperJsonObject;
	private final PetService petService;

	public PetCreationReceiverMessage(MapperJsonObject mapperJsonObject, PetService petService) {
		this.mapperJsonObject = mapperJsonObject;
		this.petService = petService;
	}

	@RabbitListener(queues = "${user.pet.creation.queue-name}")
	public void receiveMessageCreatePet(String message) {
		try {
			PetDomain pet = mapperJsonObject.fromJson(message, PetDomain.class);

			petService.createPet(pet);

		} catch (Exception e) {
			System.err.println(
					"Error al recibir la creación de Pet: "
							+ e.getMessage()
			);
			throw e;
		}
	}
}