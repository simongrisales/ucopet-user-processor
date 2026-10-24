package com.uco.ucopetuserprocessor.messaging.doctor;

import com.uco.ucopetuserprocessor.domain.doctor.DoctorDomain;
import com.uco.ucopetuserprocessor.service.doctor.DoctorService;
import com.uco.ucopetuserprocessor.util.gson.MapperJsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DoctorDeactivateReceiverMessage {

    private final DoctorService doctorService;
    private final MapperJsonObject mapperJsonObject;

    public DoctorDeactivateReceiverMessage(DoctorService doctorService, MapperJsonObject mapperJsonObject) {
        this.doctorService = doctorService;
        this.mapperJsonObject = mapperJsonObject;
    }

    @RabbitListener(queues = "${ucopet.rabbitmq.user.doctor.deactivate.queue}")
    public void listenDeactivate(String message) {
        try {
            doctorService.deactivateFromMessage(getDoctor(message));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private DoctorDomain getDoctor(String message) {
        return mapperJsonObject.fromJson(message, DoctorDomain.class);
    }
}