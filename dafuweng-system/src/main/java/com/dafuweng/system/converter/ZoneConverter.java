package com.dafuweng.system.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysZone;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZoneConverter extends EntityConverter<SysZone, ZoneVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysZone toEntity(ZoneCreateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "zoneCode", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysZone toEntity(ZoneUpdateRequest request);

    ZoneVO toVO(SysZone entity);

    void copy(ZoneUpdateRequest request, @MappingTarget SysZone entity);

    List<ZoneVO> toVOList(List<SysZone> entities);
}
