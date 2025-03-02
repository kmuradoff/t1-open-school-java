package org.kmuradoff.openschooljava.adapter.out.postgres.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.kmuradoff.openschooljava.adapter.out.postgres.dto.TaskStatus;

@Entity
@Getter
@Setter
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "user_id")
    private String userId;
    @Enumerated(EnumType.STRING)
    @ColumnDefault("NOT_STARTED")
    private TaskStatus status;
}
