package com.uco.ucopetuserprocessor.service.doctor;

import com.uco.ucopetuserprocessor.domain.doctor.DoctorDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.repository.doctor.IDoctorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DoctorService {

    private static final String LICENSE_NUMBER_REGEX = "^[A-Za-z0-9-]+$";

    private final IDoctorRepository doctorRepository;

    public DoctorService(IDoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void saveFromMessage(DoctorDomain doctor) {
        validateFields(doctor);
        doctorRepository.save(doctor);
    }

    public void updateFromMessage(DoctorDomain doctor) {
        validateFields(doctor);
        doctorRepository.save(doctor);
    }

    public void activateFromMessage(DoctorDomain doctor) {
        validateReference(doctor);

        System.out.println("Doctor activado (evento recibido, id=" + doctor.getId() + ")");
    }

    public void deactivateFromMessage(DoctorDomain doctor) {
        validateReference(doctor);
        System.out.println("Doctor desactivado (evento recibido, id=" + doctor.getId() + ")");
    }

    private void validateFields(DoctorDomain doctor) {
        if (doctor == null) {
            throw new BusinessException("La informacion del doctor no puede ser nula.");
        }
        if (doctor.getIdPerson() == null) {
            throw new BusinessException("El id de la persona es obligatorio.");
        }
        validateUUID(doctor.getIdPerson().toString());

        String licenseNumber = doctor.getLicenseNumber();
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new BusinessException("El numero de licencia es obligatorio.");
        }
        String licenseTrimmed = licenseNumber.trim();
        if (licenseTrimmed.length() < 4 || licenseTrimmed.length() > 30) {
            throw new BusinessException("El numero de licencia debe tener una longitud de entre 4 y 30 caracteres.");
        }
        if (!licenseTrimmed.matches(LICENSE_NUMBER_REGEX)) {
            throw new BusinessException("El numero de licencia solo puede contener letras, numeros y guiones.");
        }
    }

    private void validateReference(DoctorDomain doctor) {
        if (doctor == null || doctor.getId() == null) {
            throw new BusinessException("El id del doctor es obligatorio para procesar este evento.");
        }
        validateUUID(doctor.getId().toString());
    }

    public UUID validateUUID(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            throw new BusinessException("El ID proporcionado no puede ser nulo o estar vacio.");
        }

        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException _) {
            throw new BusinessException("El tipo de dato proporcionado no es un UUID valido: " + uuid);
        }
    }
}