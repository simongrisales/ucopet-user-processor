package com.uco.ucopetuserprocessor.service.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import com.uco.ucopetuserprocessor.repository.tutorPet.TutorPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TutorPetService {

    private final TutorPetRepository tutorPetRepository;

    public TutorPetService(TutorPetRepository tutorPetRepository) {
        this.tutorPetRepository = tutorPetRepository;
    }

    @Transactional
    public void create(TutorPetDomain tutorPet) {
        tutorPetRepository.save(tutorPet);
    }

    @Transactional
    public void update(UUID id, TutorPetDomain tutorPet) {
        TutorPetDomain existingTutorPet = tutorPetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TutorPet no encontrado con id: " + id));
        existingTutorPet.setPerson(tutorPet.getPerson());
        tutorPetRepository.save(existingTutorPet);
    }

    @Transactional
    public void deactivate(UUID id) {
        if (!tutorPetRepository.existsById(id)) {
            throw new IllegalArgumentException("TutorPet no encontrado con id: " + id);
        }
        tutorPetRepository.deleteById(id);
    }
}