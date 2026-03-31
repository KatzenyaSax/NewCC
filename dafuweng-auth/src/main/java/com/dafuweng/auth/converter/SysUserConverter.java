package com.dafuweng.auth.converter;

import com.dafuweng.auth.domain.dto.UserVO;
import com.dafuweng.auth.entity.SysUser;
import com.dafuweng.common.core.converter.EntityConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * SysUser转换器
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SysUserConverter extends EntityConverter<SysUser, UserVO> {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "deptName", expression = "java(getDeptName(source))")
    @Mapping(target = "zoneName", expression = "java(getZoneName(source))")
    UserVO toVO(SysUser entity);

    default String getDeptName(SysUser source) {
        // TODO: 从部门服务获取部门名称
        return null;
    }

    default String getZoneName(SysUser source) {
        // TODO: 从战区服务获取战区名称
        return null;
    }
}
