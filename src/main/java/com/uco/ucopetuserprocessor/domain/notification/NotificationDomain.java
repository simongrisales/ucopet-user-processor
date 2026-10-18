package com.uco.ucopetuserprocessor.domain.notification;

import com.uco.ucopetuserprocessor.exception.BusinessException;
import com.uco.ucopetuserprocessor.model.notification.enums.NotificationType;
import com.uco.ucopetuserprocessor.model.notification.enums.ReferenceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationDomain {

    private static final int TITLE_MAX = 150;
    private static final int MESSAGE_MAX = 255;

    @Id
    private UUID id;

    @Column(name = "person_id", nullable = false)
    private UUID personId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public NotificationDomain() {
    }

    // Business methods
    public void validate() {
        if (id == null)
            throw new BusinessException("El id de la notificación es obligatorio");
        if (personId == null)
            throw new BusinessException("El destinatario es obligatorio");

        if (title == null || title.isBlank())
            throw new BusinessException("El título es obligatorio");
        if (title.length() > TITLE_MAX)
            throw new BusinessException("El título no puede superar " + TITLE_MAX + " caracteres");

        if (message == null || message.isBlank())
            throw new BusinessException("El mensaje es obligatorio");
        if (message.length() > MESSAGE_MAX)
            throw new BusinessException("El mensaje no puede superar " + MESSAGE_MAX + " caracteres");

        if (type == null)
            throw new BusinessException("El tipo de notificación es obligatorio");

        if (read)
            throw new BusinessException("Una notificación nueva no puede nacer leída");

        if (createdAt == null)
            throw new BusinessException("La fecha de creación es obligatoria");

        // referencia: o vienen las dos o no viene ninguna
        if ((referenceId == null) != (referenceType == null))
            throw new BusinessException("referenceId y referenceType deben venir juntos");

        // el tipo de notificación debe coincidir con su referencia
        if (referenceType != null && referenceType != expectedReference(type))
            throw new BusinessException("El tipo de referencia no corresponde al tipo de notificación");
    }

    private static ReferenceType expectedReference(NotificationType type) {
        return switch (type) {
            case APPOINTMENT_CREATED -> ReferenceType.APPOINTMENT;
            case ORDER_AUTHORIZED, ORDER_REJECTED, ORDER_PENDING -> ReferenceType.ORDER;
            case LOGIN_SUCCEDED -> ReferenceType.LOGIN;
        };
    }

    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPersonId() { return personId; }
    public void setPersonId(UUID personId) { this.personId = personId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public UUID getReferenceId() { return referenceId; }
    public void setReferenceId(UUID referenceId) { this.referenceId = referenceId; }

    public ReferenceType getReferenceType() { return referenceType; }
    public void setReferenceType(ReferenceType referenceType) { this.referenceType = referenceType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}