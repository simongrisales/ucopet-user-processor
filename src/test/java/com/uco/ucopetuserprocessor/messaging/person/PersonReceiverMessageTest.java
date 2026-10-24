package com.uco.ucopetuserprocessor.messaging.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uco.ucopetuserprocessor.dto.person.DocumentType;
import com.uco.ucopetuserprocessor.dto.person.PersonMessageDTO;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.service.person.PersonService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObjectJackson;

import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Receivers de persona: que hacen con cada mensaje que llega")
class PersonReceiverMessageTest {

    @Mock
    private PersonService personService;

    private PersonCreationReceiverMessage creationReceiver;
    private PersonUpdateReceiverMessage updateReceiver;
    private PersonDeactivationReceiverMessage deactivationReceiver;

    @BeforeEach
    void setUp() {
        // El mapper de JSON es el real: asi se prueba tambien que el JSON del API se entiende.
        MapperJsonObject mapper = new MapperJsonObjectJackson(JsonMapper.builder().build());
        creationReceiver = new PersonCreationReceiverMessage(mapper, personService);
        updateReceiver = new PersonUpdateReceiverMessage(mapper, personService);
        deactivationReceiver = new PersonDeactivationReceiverMessage(mapper, personService);
    }

    @Test
    @DisplayName("crear: convierte el JSON del API y se lo pasa al service")
    void creationParsesAndDelegates() {
        UUID id = UUID.randomUUID();
        String json = """
                {"id":"%s","documentType":"CC","documentNumber":"10203040","firstName":"Ana",
                 "lastName":"Prueba","email":"ana@ucopet.com","address":null,"phone":null,
                 "admin":false,"active":true}
                """.formatted(id);

        creationReceiver.receiveMessageCreatePerson(json);

        ArgumentCaptor<PersonMessageDTO> captor = ArgumentCaptor.forClass(PersonMessageDTO.class);
        verify(personService).create(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(id);
        assertThat(captor.getValue().documentType()).isEqualTo(DocumentType.CC);
        assertThat(captor.getValue().admin()).isFalse();
    }

    @Test
    @DisplayName("actualizar: admin y active en null llegan en null (no se tocan)")
    void updateKeepsNullFlags() {
        UUID id = UUID.randomUUID();
        String json = """
                {"id":"%s","documentType":"CC","documentNumber":"10203040","firstName":"Ana",
                 "lastName":"Prueba","admin":null,"active":null}
                """.formatted(id);

        updateReceiver.receiveMessageUpdatePerson(json);

        ArgumentCaptor<PersonMessageDTO> captor = ArgumentCaptor.forClass(PersonMessageDTO.class);
        verify(personService).update(captor.capture());
        assertThat(captor.getValue().admin()).isNull();
        assertThat(captor.getValue().active()).isNull();
    }

    @Test
    @DisplayName("dar de baja: solo necesita el id")
    void deactivationOnlyNeedsId() {
        UUID id = UUID.randomUUID();

        deactivationReceiver.receiveMessageDeactivatePerson("{\"id\":\"" + id + "\"}");

        verify(personService).deactivate(id);
    }

    @Test
    @DisplayName("un error de negocio no se lanza: si se lanzara, Rabbit reintentaria el mensaje para siempre")
    void businessErrorIsNotRethrown() {
        doThrow(new BusinessException("Ya hay una persona registrada con ese correo"))
                .when(personService).update(any(PersonMessageDTO.class));
        String json = "{\"id\":\"" + UUID.randomUUID() + "\",\"documentType\":\"CC\"}";

        assertThatCode(() -> updateReceiver.receiveMessageUpdatePerson(json)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("un JSON roto se descarta sin llegar al service")
    void brokenJsonIsDiscarded() {
        assertThatCode(() -> creationReceiver.receiveMessageCreatePerson("esto no es json"))
                .doesNotThrowAnyException();
        verifyNoInteractions(personService);
    }
}