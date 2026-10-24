package com.uco.ucopetuserprocessor.dto.person;

// Copia del enum del API: los dos lados tienen que tener exactamente los mismos valores,
// porque viajan por nombre en el JSON del mensaje y se guardan como texto en la tabla persons.
public enum DocumentType {
    CC("Cedula de ciudadania"),
    TI("Tarjeta de identidad"),
    CE("Cedula de extranjeria"),
    PA("Pasaporte"),
    RC("Registro civil"),
    PPT("Permiso por proteccion temporal");

    private final String descripcion;

    DocumentType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}