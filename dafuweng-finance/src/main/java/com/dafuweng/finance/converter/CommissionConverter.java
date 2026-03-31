package com.dafuweng.finance.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.finance.domain.dto.CommissionRecordVO;
import com.dafuweng.finance.entity.CommissionRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommissionConverter extends EntityConverter<CommissionRecord, CommissionRecordVO> {

    List<CommissionRecordVO> toVOList(List<CommissionRecord> entities);
}
