package com.uco.ucopetuserprocessor.repository.person;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uco.ucopetuserprocessor.domain.person.PersonDomain;
import com.uco.ucopetuserprocessor.model.person.enums.DocumentType;

@Repository
public interface PersonRepository extends JpaRepository<PersonDomain, UUID> {

    Optional<PersonDomain> findByEmailIgnoreCase(String email);

    Optional<PersonDomain> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    long countByAdminTrueAndActiveTrue();
}