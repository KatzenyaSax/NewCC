package com.dafuweng.finance.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.finance.domain.dto.BankVO;
import com.dafuweng.finance.entity.Bank;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankConverter extends EntityConverter<Bank, BankVO> {

    BankVO toVO(Bank entity);

    List<BankVO> toVOList(List<Bank> entities);
}
