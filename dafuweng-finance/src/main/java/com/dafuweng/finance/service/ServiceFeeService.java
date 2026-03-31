package com.dafuweng.finance.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordCreateRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordPageRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordVO;
import com.dafuweng.finance.entity.ServiceFeeRecord;

import java.util.List;

public interface ServiceFeeService {

    Long create(ServiceFeeRecordCreateRequest request);

    ServiceFeeRecordVO getById(Long id);

    PageResult<ServiceFeeRecordVO> page(ServiceFeeRecordPageRequest request);

    List<ServiceFeeRecordVO> getByContractId(Long contractId);
}
