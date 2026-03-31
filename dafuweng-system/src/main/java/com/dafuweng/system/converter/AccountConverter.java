package com.dafuweng.system.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysAccount;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountConverter extends EntityConverter<SysAccount, AccountVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "loginErrorCount", constant = "0")
    @Mapping(target = "lockTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    SysAccount toEntity(AccountCreateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "loginErrorCount", ignore = true)
    @Mapping(target = "lockTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    SysAccount toEntity(AccountUpdateRequest request);

    AccountVO toVO(SysAccount entity);

    List<AccountVO> toVOList(List<SysAccount> entities);
}
