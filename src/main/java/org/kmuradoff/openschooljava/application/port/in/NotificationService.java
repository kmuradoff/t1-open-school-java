package org.kmuradoff.openschooljava.application.port.in;

import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;

public interface NotificationService {
    void sendEmailNotification(EmailNotificationDto emailNotificationDto);
}
