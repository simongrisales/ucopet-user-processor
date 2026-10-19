package com.uco.ucopetuserprocessor.repository.pet;

import com.uco.ucopetuserprocessor.domain.pet.PetDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IPetRepository extends JpaRepository<PetDomain, UUID> {

}
