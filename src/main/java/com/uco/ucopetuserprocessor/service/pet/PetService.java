package com.uco.ucopetuserprocessor.service.pet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uco.ucopetuserprocessor.domain.pet.PetDomain;
import com.uco.ucopetuserprocessor.repository.pet.IPetRepository;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.model.notification.enums.NotificationType;
import com.uco.ucopetuserprocessor.service.notification.NotificationEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class PetService {

    private final IPetRepository iPetRepository;
    private final NotificationEmitter notificationEmitter;

    private static final String TEXT_REGEX = "\\S+";
    private static final String GENDER_REGEX = "^[MF]$";
    private static final String PET_NOT_FOUND_MESSAGE =
            "No se encontró la mascota con el ID: ";

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 100;

    private static final int BREED_MIN_LENGTH = 2;
    private static final int BREED_MAX_LENGTH = 50;

    private static final int SPECIES_MIN_LENGTH = 3;
    private static final int SPECIES_MAX_LENGTH = 50;

    private static final int PHOTO_URL_MAX_LENGTH = 500;

    private static final ZoneId ZONE_ID = ZoneId.of("America/Bogota");

    public PetService(IPetRepository iPetRepository,
                      NotificationEmitter notificationEmitter) {
        this.iPetRepository = iPetRepository;
        this.notificationEmitter = notificationEmitter;
    }


    @Transactional
    public void createPet(PetDomain dto) {

        validateFields(dto);

        validateReferences(dto);


        dto.setActive(true);
        dto.setCreatedDate(LocalDateTime.now(ZONE_ID));

        iPetRepository.save(dto);

        notificationEmitter.emit(
                NotificationType.PET_CREATED,
                "Mascota creada",
                "La mascota " + dto.getName().trim() + " fue creada correctamente."
        );
    }

    @Transactional
    public void updatePet(UUID id, PetDomain dto) {

        PetDomain existingPet = iPetRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        PET_NOT_FOUND_MESSAGE + id
                ));

        validateFields(dto);

        validateReferences(dto);

        existingPet.setName(dto.getName().trim());
        existingPet.setBirthDate(dto.getBirthDate());
        existingPet.setBreed(dto.getBreed().trim());
        existingPet.setSpecies(dto.getSpecies().trim());
        existingPet.setGender(dto.getGender().trim());

        if (dto.getPhotoUrl() != null) {
            existingPet.setPhotoUrl(dto.getPhotoUrl().trim());
        } else {
            existingPet.setPhotoUrl(null);
        }

        existingPet.setTutorId(dto.getTutorId());
        existingPet.setPolicyId(dto.getPolicyId());
        existingPet.setHeadquarterId(dto.getHeadquarterId());

        existingPet.setModifiedDate(LocalDateTime.now(ZONE_ID));

        iPetRepository.save(existingPet);

        notificationEmitter.emit(
                NotificationType.PET_UPDATED,
                "Mascota actualizada",
                "La mascota " + existingPet.getName() + " fue actualizada correctamente."
        );
    }

    @Transactional
    public void deactivatePet(UUID id) {

        PetDomain existingPet = iPetRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        PET_NOT_FOUND_MESSAGE + id
                ));

        existingPet.setActive(false);
        existingPet.setModifiedDate(LocalDateTime.now(ZONE_ID));

        iPetRepository.save(existingPet);

        notificationEmitter.emit(
                NotificationType.PET_DELETED,
                "Mascota eliminada",
                "La mascota " + existingPet.getName() + " fue eliminada correctamente."
        );
    }

    private void validateFields(PetDomain dto) {

        if (dto == null) {
            throw new BusinessException(
                    "La información de la mascota no puede ser nula."
            );
        }

        validateNameField(dto.getName());
        validateSpeciesField(dto.getSpecies());
        validateBreedField(dto.getBreed());
        validateGenderField(dto.getGender());
        validateBirthDate(dto.getBirthDate());
        validatePhotoUrl(dto.getPhotoUrl());
        validateTutorId(dto.getTutorId());
        validateHeadquarterId(dto.getHeadquarterId());
    }

    private void validateNameField(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(
                    "El nombre de la mascota es obligatorio."
            );
        }

        if (!name.matches(TEXT_REGEX)) {
            throw new BusinessException(
                    "El nombre de la mascota no puede contener espacios."
            );
        }

        validateName(name);
    }

    private void validateSpeciesField(String species) {

        if (species == null || species.trim().isEmpty()) {
            throw new BusinessException(
                    "La especie es obligatoria."
            );
        }

        if (!species.matches(TEXT_REGEX)) {
            throw new BusinessException(
                    "La especie no puede contener espacios."
            );
        }

        validateSpecies(species);
    }

    private void validateBreedField(String breed) {

        if (breed == null || breed.trim().isEmpty()) {
            throw new BusinessException(
                    "La raza es obligatoria."
            );
        }

        validateBreed(breed);
    }

    private void validateReferences(PetDomain dto) {

        if (dto.getTutorId() == null) {
            throw new BusinessException(
                    "El tutor es obligatorio."
            );
        }

        if (dto.getHeadquarterId() == null) {
            throw new BusinessException(
                    "La sede es obligatoria."
            );
        }

        validateUUID(dto.getTutorId().toString());

        validateUUID(dto.getHeadquarterId().toString());

        if (dto.getPolicyId() != null) {
            validateUUID(dto.getPolicyId().toString());
        }
    }

    private void validateGenderField(String gender) {

        if (gender == null || gender.trim().isEmpty()) {
            throw new BusinessException(
                    "El género es obligatorio."
            );
        }

        if (!gender.trim().matches(GENDER_REGEX)) {
            throw new BusinessException(
                    "El género debe ser M o F."
            );
        }
    }

    private void validateBirthDate(LocalDate birthDate) {

        if (birthDate != null
                && birthDate.isAfter(LocalDate.now(ZONE_ID))) {

            throw new BusinessException(
                    "La fecha de nacimiento no puede ser futura."
            );
        }
    }

    private void validatePhotoUrl(String photoUrl) {

        if (photoUrl != null
                && photoUrl.trim().length() > PHOTO_URL_MAX_LENGTH) {

            throw new BusinessException(
                    "La URL de la foto no puede superar los "
                            + PHOTO_URL_MAX_LENGTH + " caracteres."
            );
        }
    }

    private void validateTutorId(UUID tutorId) {

        if (tutorId == null) {
            throw new BusinessException(
                    "El ID del tutor es obligatorio."
            );
        }
    }

    private void validateHeadquarterId(UUID headquarterId) {

        if (headquarterId == null) {
            throw new BusinessException(
                    "El ID de la sede es obligatorio."
            );
        }
    }

    private void validateName(String name) {

        String nameTrimmed = name.trim();

        if (nameTrimmed.length() < NAME_MIN_LENGTH
                || nameTrimmed.length() > NAME_MAX_LENGTH) {

            throw new BusinessException(
                    "El nombre debe tener una longitud de entre "
                            + NAME_MIN_LENGTH + " y "
                            + NAME_MAX_LENGTH + " caracteres."
            );
        }
    }

    private void validateBreed(String breed) {

        String breedTrimmed = breed.trim();

        if (breedTrimmed.length() < BREED_MIN_LENGTH
                || breedTrimmed.length() > BREED_MAX_LENGTH) {

            throw new BusinessException(
                    "La raza debe tener una longitud de entre "
                            + BREED_MIN_LENGTH + " y "
                            + BREED_MAX_LENGTH + " caracteres."
            );
        }
    }

    private void validateSpecies(String species) {

        String speciesTrimmed = species.trim();

        if (speciesTrimmed.length() < SPECIES_MIN_LENGTH
                || speciesTrimmed.length() > SPECIES_MAX_LENGTH) {

            throw new BusinessException(
                    "La especie debe tener una longitud de entre "
                            + SPECIES_MIN_LENGTH + " y "
                            + SPECIES_MAX_LENGTH + " caracteres."
            );
        }
    }

    public UUID validateUUID(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            throw new BusinessException("El ID proporcionado no puede ser nulo o estar vacío.");
        }

        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException _) {
            throw new BusinessException("El tipo de dato proporcionado no es un UUID válido: " + uuid);
        }
    }
}