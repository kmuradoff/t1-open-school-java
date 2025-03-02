package org.kmuradoff.openschooljava.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;

@Getter
@Setter
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String userId;
    private TaskStatus status;
}
