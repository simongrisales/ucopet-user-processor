package com.uco.ucopetuserprocessor.service.notification;

import com.uco.ucopetuserprocessor.domain.notification.NotificationDomain;
import com.uco.ucopetuserprocessor.repository.notification.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void create(NotificationDomain notification) {
        notification.validate();
        if (repository.existsById(notification.getId())) {
            return;
        }
        repository.save(notification);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            return;                       // no existe: terminar sin error
        }
        repository.deleteById(id);
    }
}