package com.uco.ucopetuserprocessor.service.doctor;

import com.uco.ucopetuserprocessor.domain.doctor.DoctorDomain;
import com.uco.ucopetuserprocessor.repository.doctor.IDoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    private final IDoctorRepository doctorRepository;

    public DoctorService(IDoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void saveFromMessage(DoctorDomain doctor) {
        doctorRepository.save(doctor);
    }

    public void updateFromMessage(DoctorDomain doctor) {
        doctorRepository.save(doctor);
    }

    public void activateFromMessage(DoctorDomain doctor) {
        // No se persiste nada en la tabla doctors: el cambio real ya ocurrio
        // sobre Person (en ucopetapi) antes de publicar este mensaje. Este
        // listener solo deja traza de que el evento llego.
        System.out.println("Doctor activado (evento recibido, id=" + doctor.getId() + ")");
    }

    public void deactivateFromMessage(DoctorDomain doctor) {
        // Mismo caso que activateFromMessage.
        System.out.println("Doctor desactivado (evento recibido, id=" + doctor.getId() + ")");
    }
}