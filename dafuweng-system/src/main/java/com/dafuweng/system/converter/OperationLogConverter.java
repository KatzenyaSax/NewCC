package com.dafuweng.system.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysOperationLog;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationLogConverter extends EntityConverter<SysOperationLog, OperationLogVO> {

    OperationLogVO toVO(SysOperationLog entity);

    List<OperationLogVO> toVOList(List<SysOperationLog> entities);
}
