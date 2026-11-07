package com.uco.ucopetuserprocessor.service.specialtieDoctor;

import com.uco.ucopetuserprocessor.domain.specialtieDoctor.SpecialtieDoctorDomain;
import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.repository.specialtieDoctor.ISpecialtieDoctorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SpecialtieDoctorService {

    private final ISpecialtieDoctorRepository specialtieDoctorRepository;

    public SpecialtieDoctorService(ISpecialtieDoctorRepository specialtieDoctorRepository) {
        this.specialtieDoctorRepository = specialtieDoctorRepository;
    }

    public void saveFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        validateFields(specialtieDoctor);
        specialtieDoctorRepository.save(specialtieDoctor);
    }

    public void updateFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        validateFields(specialtieDoctor);
        specialtieDoctorRepository.save(specialtieDoctor);
    }

    public void deactivateFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        if (specialtieDoctor == null || specialtieDoctor.getId() == null) {
            throw new BusinessException("El id de la relacion doctor-especialidad es obligatorio para procesar este evento.");
        }
        validateUUID(specialtieDoctor.getId().toString());

        specialtieDoctorRepository.deleteById(specialtieDoctor.getId());
    }

    private void validateFields(SpecialtieDoctorDomain specialtieDoctor) {
        if (specialtieDoctor == null) {
            throw new BusinessException("La informacion de la relacion doctor-especialidad no puede ser nula.");
        }
        if (specialtieDoctor.getDoctor() == null || specialtieDoctor.getDoctor().getId() == null) {
            throw new BusinessException("El id del doctor es obligatorio.");
        }
        validateUUID(specialtieDoctor.getDoctor().getId().toString());

        if (specialtieDoctor.getIdSpecialtie() == null) {
            throw new BusinessException("El id de la especialidad es obligatorio.");
        }
        validateUUID(specialtieDoctor.getIdSpecialtie().toString());
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