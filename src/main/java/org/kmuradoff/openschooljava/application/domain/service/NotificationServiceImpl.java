package org.kmuradoff.openschooljava.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.kmuradoff.openschooljava.application.domain.config.EmailProperty;
import org.kmuradoff.openschooljava.application.domain.dto.EmailNotificationDto;
import org.kmuradoff.openschooljava.application.port.in.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final EmailProperty emailProperty;

    @Override
    public void sendEmailNotification(EmailNotificationDto emailNotificationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperty.getFrom());
        message.setTo(emailProperty.getTo());
        message.setSubject(emailProperty.getSubject());

        message.setText(String.format(
                "TASK STATUS FOR ID - %s - CHANGED TO - %s",
                emailNotificationDto.getTaskId(),
                emailNotificationDto.getTaskStatus())
        );

        mailSender.send(message);
    }
}
