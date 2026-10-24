package com.uco.ucopetuserprocessor.dto.person;

import java.util.UUID;

// Lo que publica el API en las colas de persona (crear, actualizar y desactivar).
//  - id: lo genera el API antes de publicar, para poder devolverlo en la respuesta y consultarlo despues.
//  - admin / active: null = "no lo toques". El API los manda en null cuando quien edita no es ADMIN.
//  - Para desactivar basta con el id; el resto de campos puede venir vacio.
public record PersonMessageDTO(
        UUID id,
        DocumentType documentType,
        String documentNumber,
        String firstName,
        String lastName,
        String email,
        String address,
        String phone,
        Boolean admin,
        Boolean active) {
}