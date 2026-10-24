package com.uco.ucopetuserprocessor.service.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.dto.person.DocumentType;
import com.uco.ucopetuserprocessor.dto.person.PersonMessageDTO;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.repository.person.PersonRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonService del procesador (unitario, sin base de datos)")
class PersonServiceTest {

    private static final String EMAIL = "ana@ucopet.com";

    @Mock
    private PersonRepository personRepository;

    private PersonService service;

    @BeforeEach
    void setUp() {
        service = new PersonService(personRepository);
    }

    @Nested
    @DisplayName("Crear")
    class Create {

        @Test
        @DisplayName("guarda la persona con el id que genero el API, activa y con el correo normalizado")
        void savesWithApiId() {
            UUID id = UUID.randomUUID();

            service.create(message(id, "  Ana@UCOPET.com ", false, null));

            ArgumentCaptor<PersonDomain> saved = ArgumentCaptor.forClass(PersonDomain.class);
            verify(personRepository).saveAndFlush(saved.capture());
            assertThat(saved.getValue().getId()).isEqualTo(id);
            assertThat(saved.getValue().getEmail()).isEqualTo(EMAIL);
            assertThat(saved.getValue().isActive()).isTrue();
            assertThat(saved.getValue().isAdmin()).isFalse();
        }

        @Test
        @DisplayName("si el mensaje llega dos veces, la segunda no crea nada")
        void duplicatedMessageIsIgnored() {
            UUID id = UUID.randomUUID();
            when(personRepository.existsById(id)).thenReturn(true);

            assertThatThrownBy(() -> service.create(message(id, EMAIL, false, null)))
                    .isInstanceOf(BusinessException.class);
            verify(personRepository, never()).saveAndFlush(any(PersonDomain.class));
        }

        @Test
        @DisplayName("un correo repetido (con otras mayusculas) no se guarda")
        void duplicatedEmail() {
            when(personRepository.findByEmailIgnoreCase(EMAIL))
                    .thenReturn(Optional.of(person(UUID.randomUUID(), false, true)));

            assertThatThrownBy(() -> service.create(message(UUID.randomUUID(), "ANA@ucopet.com", false, null)))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("correo");
            verify(personRepository, never()).saveAndFlush(any(PersonDomain.class));
        }

        @Test
        @DisplayName("un mensaje sin id o sin documento no se guarda")
        void missingRequiredData() {
            PersonMessageDTO withoutId = message(null, EMAIL, false, null);

            assertThatThrownBy(() -> service.create(withoutId))
                    .isInstanceOf(BusinessException.class);
            verify(personRepository, never()).saveAndFlush(any(PersonDomain.class));
        }
    }

    @Nested
    @DisplayName("Actualizar")
    class Update {

        @Test
        @DisplayName("admin y active en null no cambian nada (asi llegan cuando no edita un ADMIN)")
        void nullFlagsDoNotChange() {
            PersonDomain ana = person(UUID.randomUUID(), false, true);
            when(personRepository.findById(ana.getId())).thenReturn(Optional.of(ana));

            service.update(message(ana.getId(), EMAIL, null, null));

            assertThat(ana.isAdmin()).isFalse();
            assertThat(ana.isActive()).isTrue();
            assertThat(ana.getTokenVersion()).isZero();
            verify(personRepository).saveAndFlush(ana);
        }

        @Test
        @DisplayName("dar el rol ADMIN invalida los tokens de esa persona")
        void grantingAdminRevokesTokens() {
            PersonDomain ana = person(UUID.randomUUID(), false, true);
            when(personRepository.findById(ana.getId())).thenReturn(Optional.of(ana));

            service.update(message(ana.getId(), EMAIL, true, null));

            assertThat(ana.isAdmin()).isTrue();
            assertThat(ana.getTokenVersion()).isEqualTo(1);
        }

        @Test
        @DisplayName("no se le puede quitar el rol al unico ADMIN activo")
        void cannotRevokeLastAdmin() {
            PersonDomain admin = person(UUID.randomUUID(), true, true);
            when(personRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
            when(personRepository.countByAdminTrueAndActiveTrue()).thenReturn(1L);

            assertThatThrownBy(() -> service.update(message(admin.getId(), EMAIL, false, null)))
                    .isInstanceOf(BusinessException.class);
            verify(personRepository, never()).saveAndFlush(any(PersonDomain.class));
        }

        @Test
        @DisplayName("si la persona no existe, avisa con BusinessException")
        void unknownPerson() {
            UUID id = UUID.randomUUID();
            when(personRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(message(id, EMAIL, null, null)))
                    .isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    @DisplayName("Dar de baja")
    class Deactivate {

        @Test
        @DisplayName("la deja inactiva y llena deactivatedAt")
        void deactivates() {
            PersonDomain ana = person(UUID.randomUUID(), false, true);
            when(personRepository.findById(ana.getId())).thenReturn(Optional.of(ana));

            service.deactivate(ana.getId());

            assertThat(ana.isActive()).isFalse();
            assertThat(ana.getDeactivatedAt()).isNotNull();
            verify(personRepository).save(ana);
        }

        @Test
        @DisplayName("no deja dar de baja al unico ADMIN activo")
        void cannotDeactivateLastAdmin() {
            PersonDomain admin = person(UUID.randomUUID(), true, true);
            when(personRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
            when(personRepository.countByAdminTrueAndActiveTrue()).thenReturn(1L);

            assertThatThrownBy(() -> service.deactivate(admin.getId()))
                    .isInstanceOf(BusinessException.class);
            assertThat(admin.isActive()).isTrue();
        }
    }

    // ---------------------------------------------------------------- helpers

    private static PersonMessageDTO message(UUID id, String email, Boolean admin, Boolean active) {
        return new PersonMessageDTO(id, DocumentType.CC, "10203040", "Ana", "Prueba", email,
                "Calle 1 # 2-3, Rionegro", "3000000000", admin, active);
    }

    private static PersonDomain person(UUID id, boolean admin, boolean active) {
        PersonDomain person = new PersonDomain();
        person.setId(id);
        person.setDocumentType(DocumentType.CC);
        person.setDocumentNumber("10203040");
        person.setFirstName("Ana");
        person.setLastName("Prueba");
        person.setEmail(EMAIL);
        person.setAdmin(admin);
        person.setActive(active);
        return person;
    }
}