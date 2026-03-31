package com.dafuweng.finance.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordCreateRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordVO;
import com.dafuweng.finance.entity.ServiceFeeRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceFeeConverter extends EntityConverter<ServiceFeeRecord, ServiceFeeRecordVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    ServiceFeeRecord toEntity(ServiceFeeRecordCreateRequest request);

    List<ServiceFeeRecordVO> toVOList(List<ServiceFeeRecord> entities);
}
