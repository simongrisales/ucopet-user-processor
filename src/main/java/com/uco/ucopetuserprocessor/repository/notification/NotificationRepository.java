package com.uco.ucopetuserprocessor.repository.notification;

import com.uco.ucopetuserprocessor.domain.notification.NotificationDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationDomain, UUID> {

}
