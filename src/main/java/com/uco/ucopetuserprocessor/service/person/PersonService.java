package com.uco.ucopetuserprocessor.service.person;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.dto.person.PersonMessageDTO;
import com.uco.ucopetuserprocessor.repository.person.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void create(PersonMessageDTO message) {
        validateRequiredData(message);
        if (personRepository.existsById(message.id())) {
            throw new BusinessException("Ya existe una persona con el id " + message.id()
                    + ": el mensaje de creacion ya se habia procesado");
        }
        validateUniqueness(message, message.id());

        PersonDomain created = new PersonDomain();
        created.setId(message.id());
        copyData(message, created);
        created.setAdmin(Boolean.TRUE.equals(message.admin()));
        created.setActive(true);

        saveOrConflict(created);
    }

    @Transactional
    public void update(PersonMessageDTO message) {
        validateRequiredData(message);
        PersonDomain current = findOrThrow(message.id());
        validateUniqueness(message, current.getId());

        copyData(message, current);
        applyAdminFlag(current, message.admin());
        applyActiveFlag(current, message.active());

        saveOrConflict(current);
    }

    @Transactional
    public void deactivate(UUID personId) {
        if (personId == null) {
            throw new BusinessException("El mensaje de baja no trae el id de la persona");
        }
        PersonDomain current = findOrThrow(personId);
        validateNotLastAdmin(current);
        current.setActive(false);
        syncDeactivation(current);
        personRepository.save(current);
    }

    private void validateRequiredData(PersonMessageDTO message) {
        if (message.id() == null || message.documentType() == null
                || isBlank(message.documentNumber())
                || isBlank(message.firstName()) || isBlank(message.lastName())) {
            throw new BusinessException(
                    "El mensaje no trae los datos obligatorios de la persona (id, documento, nombre y apellido)");
        }
    }

    private void copyData(PersonMessageDTO desde, PersonDomain hacia) {
        hacia.setDocumentType(desde.documentType());
        hacia.setDocumentNumber(desde.documentNumber());
        hacia.setFirstName(desde.firstName());
        hacia.setLastName(desde.lastName());
        hacia.setEmail(normalizeEmail(desde.email()));
        hacia.setAddress(desde.address());
        hacia.setPhone(desde.phone());
    }

    private void applyActiveFlag(PersonDomain current, Boolean active) {
        if (active == null || active.booleanValue() == current.isActive()) {
            return;
        }
        if (!active) {
            validateNotLastAdmin(current);
        }
        current.setActive(active);
        syncDeactivation(current);
    }

    private void applyAdminFlag(PersonDomain current, Boolean admin) {
        if (admin == null || admin.booleanValue() == current.isAdmin()) {
            return;
        }
        if (!admin) {
            validateNotLastAdmin(current);
        }
        current.setAdmin(admin);
        current.setTokenVersion(current.getTokenVersion() + 1);
    }

    private void validateNotLastAdmin(PersonDomain person) {
        if (person.isAdmin() && person.isActive()
                && personRepository.countByAdminTrueAndActiveTrue() <= 1) {
            throw new BusinessException(
                    "No se puede dejar el sistema sin administradores activos. "
                            + "Primero hay que darle el rol ADMIN a otra persona.");
        }
    }

    private void syncDeactivation(PersonDomain person) {
        if (person.isActive()) {
            person.setDeactivatedAt(null);
        } else if (person.getDeactivatedAt() == null) {
            person.setDeactivatedAt(Instant.now());
        }
    }

    private void validateUniqueness(PersonMessageDTO message, UUID personId) {
        String email = normalizeEmail(message.email());
        if (email != null) {
            personRepository.findByEmailIgnoreCase(email)
                    .filter(otra -> !otra.getId().equals(personId))
                    .ifPresent(_ -> {
                        throw new BusinessException("Ya hay una persona registrada con ese correo");
                    });
        }
        personRepository.findByDocumentTypeAndDocumentNumber(
                        message.documentType(), message.documentNumber())
                .filter(otra -> !otra.getId().equals(personId))
                .ifPresent(_ -> {
                    throw new BusinessException("Ya hay una persona registrada con ese documento");
                });
    }

    private void saveOrConflict(PersonDomain person) {
        try {
            personRepository.saveAndFlush(person);
        } catch (DataIntegrityViolationException _) {
            throw new BusinessException("El correo o el documento ya estan registrados");
        }
    }

    private PersonDomain findOrThrow(UUID personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new BusinessException("No existe una persona con el id " + personId));
    }

    private static String normalizeEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}