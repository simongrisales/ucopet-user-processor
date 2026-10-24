package com.uco.ucopetuserprocessor.service.person;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.repository.person.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void create(PersonDomain message) {
        validateRequiredData(message);
        if (personRepository.existsById(message.getId())) {
            // Rabbit puede entregar el mismo mensaje dos veces: crear dos veces no debe duplicar nada.
            throw new BusinessException("Ya existe una persona con el id " + message.getId()
                    + ": el mensaje de creacion ya se habia procesado");
        }
        validateUniqueness(message, message.getId());

        PersonDomain created = new PersonDomain();
        created.setId(message.getId());
        copyData(message, created);
        created.setAdmin(message.isAdmin());
        created.setActive(true);

        saveOrConflict(created);
    }

    @Transactional
    public void update(PersonDomain message) {
        validateRequiredData(message);
        PersonDomain current = findOrThrow(message.getId());
        validateUniqueness(message, current.getId());

        copyData(message, current);
        // El API manda siempre como deben quedar admin y active (si quien edito no es ADMIN,
        // manda los mismos que ya tenia la persona). Aca solo se cambian si son distintos.
        applyAdminFlag(current, message.isAdmin());
        applyActiveFlag(current, message.isActive());

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

    private void validateRequiredData(PersonDomain message) {
        if (message.getId() == null || message.getDocumentType() == null
                || isBlank(message.getDocumentNumber())
                || isBlank(message.getFirstName()) || isBlank(message.getLastName())) {
            throw new BusinessException(
                    "El mensaje no trae los datos obligatorios de la persona (id, documento, nombre y apellido)");
        }
    }

    private void copyData(PersonDomain desde, PersonDomain hacia) {
        hacia.setDocumentType(desde.getDocumentType());
        hacia.setDocumentNumber(desde.getDocumentNumber());
        hacia.setFirstName(desde.getFirstName());
        hacia.setLastName(desde.getLastName());
        hacia.setEmail(normalizeEmail(desde.getEmail()));
        hacia.setAddress(desde.getAddress());
        hacia.setPhone(desde.getPhone());
    }

    private void applyActiveFlag(PersonDomain current, boolean active) {
        if (active == current.isActive()) {
            return;
        }
        if (!active) {
            validateNotLastAdmin(current);
        }
        current.setActive(active);
        syncDeactivation(current);
    }

    private void applyAdminFlag(PersonDomain current, boolean admin) {
        if (admin == current.isAdmin()) {
            return;
        }
        if (!admin) {
            validateNotLastAdmin(current);
        }
        current.setAdmin(admin);
        // Los roles viajan dentro del token: se invalida para que vuelva a entrar con el rol nuevo.
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

    // El API ya valida esto antes de publicar, pero se vuelve a validar aca: entre que el API
    // publica y el procesador guarda, otro mensaje pudo haber registrado el mismo correo o documento.
    private void validateUniqueness(PersonDomain message, UUID personId) {
        String email = normalizeEmail(message.getEmail());
        if (email != null) {
            personRepository.findByEmailIgnoreCase(email)
                    .filter(otra -> !otra.getId().equals(personId))
                    .ifPresent(_ -> {
                        throw new BusinessException("Ya hay una persona registrada con ese correo");
                    });
        }
        personRepository.findByDocumentTypeAndDocumentNumber(
                        message.getDocumentType(), message.getDocumentNumber())
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

    // Los correos se guardan en minusculas y sin espacios; uno vacio se guarda como null.
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