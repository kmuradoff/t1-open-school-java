package org.kmuradoff.openschooljava.adapter.out.postgres.mapper;

import org.kmuradoff.openschooljava.adapter.out.postgres.model.TaskEntity;
import org.kmuradoff.openschooljava.application.domain.dto.TaskDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    TaskDto toDto(TaskEntity taskEntity);
    TaskEntity toEntity(TaskDto taskDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TaskDto taskDto, @MappingTarget TaskEntity taskEntity);
}
