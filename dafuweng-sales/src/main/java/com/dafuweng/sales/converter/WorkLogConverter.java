package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.dto.WorkLogSubmitRequest;
import com.dafuweng.sales.domain.vo.WorkLogVO;
import com.dafuweng.sales.entity.WorkLog;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkLogConverter extends EntityConverter<WorkLog, WorkLogVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    WorkLog toEntity(WorkLogSubmitRequest request);

    WorkLogVO toVO(WorkLog entity);
}
