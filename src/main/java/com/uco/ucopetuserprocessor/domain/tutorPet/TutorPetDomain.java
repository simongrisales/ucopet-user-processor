package com.uco.ucopetuserprocessor.domain.tutorPet;

import jakarta.persistence.Column;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "tutor_pet")
public class TutorPetDomain implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "person_id", nullable = false)
    private UUID person;

    @Transient
    private boolean newEntity = true;

    public TutorPetDomain() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return newEntity;
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

    @PostLoad
    @PostPersist
    private void markNotNew() {
        newEntity = false;
    }
}