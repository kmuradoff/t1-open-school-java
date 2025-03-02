package org.kmuradoff.openschooljava.application.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;
import org.kmuradoff.openschooljava.application.port.in.NotificationService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendEmailNotification(EmailNotificationDto emailNotificationDto) {
        log.info("sending email notification: {}", emailNotificationDto.toString());
    }
}
