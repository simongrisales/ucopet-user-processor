package com.uco.ucopetuserprocessor.messaging.specialtieDoctor;

import com.uco.ucopetuserprocessor.domain.specialtieDoctor.SpecialtieDoctorDomain;
import com.uco.ucopetuserprocessor.service.specialtieDoctor.SpecialtieDoctorService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpecialtieDoctorDeactivateReceiverMessage {

    private final SpecialtieDoctorService specialtieDoctorService;
    private final MapperJsonObject mapperJsonObject;

    public SpecialtieDoctorDeactivateReceiverMessage(SpecialtieDoctorService specialtieDoctorService,
                                                     MapperJsonObject mapperJsonObject) {
        this.specialtieDoctorService = specialtieDoctorService;
        this.mapperJsonObject = mapperJsonObject;
    }

    @RabbitListener(queues = "${ucopet.rabbitmq.user.specialtieDoctor.deactivate.queue}")
    public void listenDeactivate(String message) {
        try {
            specialtieDoctorService.deactivateFromMessage(getSpecialtieDoctor(message));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private SpecialtieDoctorDomain getSpecialtieDoctor(String message) {
        return mapperJsonObject.fromJson(message, SpecialtieDoctorDomain.class);
    }
}