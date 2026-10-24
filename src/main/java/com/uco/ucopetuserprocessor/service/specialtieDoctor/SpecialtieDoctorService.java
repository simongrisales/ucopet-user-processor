package com.uco.ucopetuserprocessor.service.specialtieDoctor;

import com.uco.ucopetuserprocessor.domain.specialtieDoctor.SpecialtieDoctorDomain;
import com.uco.ucopetuserprocessor.repository.specialtieDoctor.ISpecialtieDoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class SpecialtieDoctorService {

    private final ISpecialtieDoctorRepository specialtieDoctorRepository;

    public SpecialtieDoctorService(ISpecialtieDoctorRepository specialtieDoctorRepository) {
        this.specialtieDoctorRepository = specialtieDoctorRepository;
    }

    public void saveFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        specialtieDoctorRepository.save(specialtieDoctor);
    }

    public void updateFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        specialtieDoctorRepository.save(specialtieDoctor);
    }

    public void deactivateFromMessage(SpecialtieDoctorDomain specialtieDoctor) {
        // Esta entidad no tiene campo "active": desactivar equivale a eliminar
        // la relacion, tal como se definio del lado del publisher.
        specialtieDoctorRepository.deleteById(specialtieDoctor.getId());
    }
}