package com.uco.ucopetuserprocessor.service.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.repository.person.PersonRepository;
import com.uco.ucopetuserprocessor.repository.tutorPet.TutorPetRepository;
import com.uco.ucopetuserprocessor.service.person.PersonService;
import com.uco.ucopetuserprocessor.model.notification.enums.NotificationType;
import com.uco.ucopetuserprocessor.service.notification.NotificationEmitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Objects;

@Service
public class TutorPetService {

    private final TutorPetRepository tutorPetRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final NotificationEmitter notificationEmitter;

    public TutorPetService(TutorPetRepository tutorPetRepository,
                           PersonRepository personRepository,
                           PersonService personService,
                           NotificationEmitter notificationEmitter) {
        this.tutorPetRepository = tutorPetRepository;
        this.personRepository = personRepository;
        this.personService = personService;
        this.notificationEmitter = notificationEmitter;
    }

    @Transactional
    public void create(TutorPetDomain tutorPet) {
        validateTutorPet(tutorPet);
        TutorPetDomain existingTutorPet = tutorPetRepository.findById(tutorPet.getId()).orElse(null);
        if (existingTutorPet != null) {
            if (Objects.equals(existingTutorPet.getPerson(), tutorPet.getPerson())) {
                return;
            }
            throw new BusinessException("Ya existe un TutorPet con el identificador: " + tutorPet.getId());
        }
        validatePersonExists(tutorPet.getPerson());
        tutorPetRepository.save(tutorPet);

        notificationEmitter.emit(
                NotificationType.TUTOR_CREATED,
                "Tutor creado",
                "El tutor " + personName(tutorPet.getPerson()) + " fue creado correctamente."
        );
    }

    @Transactional
    public void update(UUID id, TutorPetDomain tutorPet) {
        if (id == null) {
            throw new BusinessException("El identificador de TutorPet es obligatorio para actualizar.");
        }
        validateTutorPet(tutorPet);
        if (!id.equals(tutorPet.getId())) {
            throw new BusinessException("El identificador del mensaje no coincide con el TutorPet a actualizar.");
        }

        TutorPetDomain existingTutorPet = tutorPetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("TutorPet no encontrado con id: " + id));
        validatePersonExists(tutorPet.getPerson());
        existingTutorPet.setPerson(tutorPet.getPerson());
        tutorPetRepository.save(existingTutorPet);

        notificationEmitter.emit(
                NotificationType.TUTOR_UPDATED,
                "Tutor actualizado",
                "El tutor " + personName(existingTutorPet.getPerson()) + " fue actualizado correctamente."
        );
    }

    @Transactional
    public void deactivate(UUID id) {
        if (id == null) {
            throw new BusinessException("El identificador de TutorPet es obligatorio para desactivar.");
        }
        TutorPetDomain existingTutorPet = tutorPetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("TutorPet no encontrado con id: " + id));
        if (existingTutorPet.getPerson() == null) {
            throw new BusinessException("El TutorPet no tiene una persona asociada para desactivar.");
        }

        personService.deactivate(existingTutorPet.getPerson());

        notificationEmitter.emit(
                NotificationType.TUTOR_DELETED,
                "Tutor eliminado",
                "El tutor " + personName(existingTutorPet.getPerson()) + " fue eliminado correctamente."
        );
    }

    private String personName(UUID personId) {
        return personRepository.findById(personId)
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("");
    }

    private void validateTutorPet(TutorPetDomain tutorPet) {
        if (tutorPet == null) {
            throw new BusinessException("La información de TutorPet es obligatoria.");
        }
        if (tutorPet.getId() == null) {
            throw new BusinessException("El identificador de TutorPet es obligatorio.");
        }
        if (tutorPet.getPerson() == null) {
            throw new BusinessException("La persona asociada al TutorPet es obligatoria.");
        }
    }

    private void validatePersonExists(UUID personId) {
        if (!personRepository.existsById(personId)) {
            throw new BusinessException("No existe una persona con el identificador: " + personId);
        }
    }
}