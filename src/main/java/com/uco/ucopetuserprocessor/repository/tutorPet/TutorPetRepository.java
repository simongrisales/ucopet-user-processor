package com.uco.ucopetuserprocessor.repository.tutorPet;

import com.uco.ucopetuserprocessor.domain.tutorPet.TutorPetDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TutorPetRepository extends JpaRepository<TutorPetDomain, UUID> {
}