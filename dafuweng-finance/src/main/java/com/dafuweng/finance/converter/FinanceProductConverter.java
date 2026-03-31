package com.dafuweng.finance.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.finance.domain.dto.FinanceProductCreateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductUpdateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductVO;
import com.dafuweng.finance.entity.FinanceProduct;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinanceProductConverter extends EntityConverter<FinanceProduct, FinanceProductVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "bankName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "onlineTime", ignore = true)
    @Mapping(target = "offlineTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    FinanceProduct toEntity(FinanceProductCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void copy(FinanceProductUpdateRequest request, @MappingTarget FinanceProduct entity);

    FinanceProductVO toVO(FinanceProduct entity);

    List<FinanceProductVO> toVOList(List<FinanceProduct> entities);
}
