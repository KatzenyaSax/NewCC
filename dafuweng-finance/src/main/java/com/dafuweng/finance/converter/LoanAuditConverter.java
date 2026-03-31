package com.dafuweng.finance.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.finance.domain.dto.LoanAuditRecordVO;
import com.dafuweng.finance.domain.dto.LoanAuditSimpleVO;
import com.dafuweng.finance.domain.dto.LoanAuditVO;
import com.dafuweng.finance.entity.LoanAudit;
import com.dafuweng.finance.entity.LoanAuditRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanAuditConverter extends EntityConverter<LoanAudit, LoanAuditVO> {

    LoanAuditVO toVO(LoanAudit entity);

    LoanAuditSimpleVO toSimpleVO(LoanAudit entity);

    LoanAuditRecordVO toRecordVO(LoanAuditRecord entity);

    List<LoanAuditSimpleVO> toSimpleVOList(List<LoanAudit> entities);

    List<LoanAuditRecordVO> toRecordVOList(List<LoanAuditRecord> entities);
}
