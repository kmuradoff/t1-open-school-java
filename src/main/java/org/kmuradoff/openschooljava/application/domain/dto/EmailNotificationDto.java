package org.kmuradoff.openschooljava.application.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationDto {

    private String taskId;
    private String taskStatus;
}
