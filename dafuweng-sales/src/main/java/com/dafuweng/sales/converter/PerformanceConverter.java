package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.vo.PerformanceVO;
import com.dafuweng.sales.entity.PerformanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PerformanceConverter extends EntityConverter<PerformanceRecord, PerformanceVO> {

    PerformanceVO toVO(PerformanceRecord entity);

    List<PerformanceVO> toVOList(List<PerformanceRecord> entities);
}
