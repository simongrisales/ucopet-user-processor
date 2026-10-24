package com.uco.ucopetuserprocessor.domain.tutorPet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tutor_pet")
public class TutorPetDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "person_id", nullable = false)
    private UUID person;

    public TutorPetDomain() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPerson() {
        return person;
    }

    public void setPerson(UUID person) {
        this.person = person;
    }
}