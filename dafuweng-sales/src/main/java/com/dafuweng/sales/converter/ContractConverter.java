package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.dto.ContractCreateRequest;
import com.dafuweng.sales.domain.vo.ContractAttachmentVO;
import com.dafuweng.sales.domain.vo.ContractVO;
import com.dafuweng.sales.entity.Contract;
import com.dafuweng.sales.entity.ContractAttachment;
import com.dafuweng.sales.enums.ContractStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractConverter extends EntityConverter<Contract, ContractVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contractNo", ignore = true)
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "deptId", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "actualLoanAmount", ignore = true)
    @Mapping(target = "serviceFee1", ignore = true)
    @Mapping(target = "serviceFee2", ignore = true)
    @Mapping(target = "serviceFee1Paid", ignore = true)
    @Mapping(target = "serviceFee2Paid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "signDate", ignore = true)
    @Mapping(target = "financeSendTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Contract toEntity(ContractCreateRequest request);

    @Named("statusDesc")
    default String statusDesc(Integer status) {
        if (status == null) return null;
        ContractStatus contractStatus = ContractStatus.fromCode(status);
        return contractStatus != null ? contractStatus.getDesc() : null;
    }

    ContractAttachmentVO toAttachmentVO(ContractAttachment entity);

    List<ContractAttachmentVO> toAttachmentVOList(List<ContractAttachment> entities);
}
