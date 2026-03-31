package com.dafuweng.finance.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.domain.dto.*;
import com.dafuweng.finance.entity.LoanAudit;

import java.util.List;

public interface LoanAuditService {

    Long receive(LoanAuditReceiveRequest request);

    Long review(LoanAuditReviewRequest request);

    Long submitBank(LoanAuditSubmitBankRequest request);

    Long bankResult(LoanAuditBankResultRequest request);

    Long reject(LoanAuditRejectRequest request);

    LoanAuditVO getById(Long id);

    PageResult<LoanAuditSimpleVO> received(PageResult<LoanAuditSimpleVO> pageRequest);

    List<LoanAuditRecordVO> history(Long id);

    LoanAudit getEntityById(Long id);
}
