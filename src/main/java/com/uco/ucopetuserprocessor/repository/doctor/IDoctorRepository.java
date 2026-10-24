package com.uco.ucopetuserprocessor.repository.doctor;

import com.uco.ucopetuserprocessor.domain.doctor.DoctorDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IDoctorRepository extends JpaRepository<DoctorDomain, UUID> {
}