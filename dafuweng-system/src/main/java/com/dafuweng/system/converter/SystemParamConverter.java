package com.dafuweng.system.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysParam;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemParamConverter extends EntityConverter<SysParam, SystemParamVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysParam toEntity(SystemParamCreateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "paramGroup", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysParam toEntity(SystemParamUpdateRequest request);

    SystemParamVO toVO(SysParam entity);

    List<SystemParamVO> toVOList(List<SysParam> entities);
}
