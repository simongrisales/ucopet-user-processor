package com.uco.ucopetuserprocessor.repository.specialtieDoctor;

import com.uco.ucopetuserprocessor.domain.specialtieDoctor.SpecialtieDoctorDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ISpecialtieDoctorRepository extends JpaRepository<SpecialtieDoctorDomain, UUID> {
}