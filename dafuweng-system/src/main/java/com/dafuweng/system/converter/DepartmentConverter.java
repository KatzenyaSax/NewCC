package com.dafuweng.system.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysDepartment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentConverter extends EntityConverter<SysDepartment, DepartmentVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysDepartment toEntity(DepartmentCreateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "deptCode", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "zoneId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SysDepartment toEntity(DepartmentUpdateRequest request);

    void copy(DepartmentUpdateRequest request, @MappingTarget SysDepartment entity);

    DepartmentVO toVO(SysDepartment entity);

    DepartmentTreeVO toTreeVO(SysDepartment entity);

    List<DepartmentVO> toVOList(List<SysDepartment> entities);

    List<DepartmentTreeVO> toTreeVOList(List<SysDepartment> entities);
}
