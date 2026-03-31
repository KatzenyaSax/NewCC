package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.dto.CustomerCreateRequest;
import com.dafuweng.sales.domain.dto.CustomerUpdateRequest;
import com.dafuweng.sales.domain.vo.CustomerVO;
import com.dafuweng.sales.entity.Customer;
import com.dafuweng.sales.enums.CustomerStatus;
import com.dafuweng.sales.enums.CustomerType;
import com.dafuweng.sales.enums.IntentionLevel;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerConverter extends EntityConverter<Customer, CustomerVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "deptId", ignore = true)
    @Mapping(target = "zoneId", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CustomerCreateRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "deptId", ignore = true)
    @Mapping(target = "zoneId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CustomerUpdateRequest request);

    @Named("customerTypeDesc")
    default String customerTypeDesc(Integer customerType) {
        if (customerType == null) return null;
        CustomerType type = CustomerType.fromCode(customerType);
        return type != null ? type.getDesc() : null;
    }

    @Named("intentionLevelDesc")
    default String intentionLevelDesc(Integer level) {
        if (level == null) return null;
        IntentionLevel intentionLevel = IntentionLevel.fromCode(level);
        return intentionLevel != null ? intentionLevel.getDesc() : null;
    }

    @Named("statusDesc")
    default String statusDesc(Integer status) {
        if (status == null) return null;
        CustomerStatus customerStatus = CustomerStatus.fromCode(status);
        return customerStatus != null ? customerStatus.getDesc() : null;
    }
}
