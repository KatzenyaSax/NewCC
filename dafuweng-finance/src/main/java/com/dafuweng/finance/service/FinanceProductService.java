package com.dafuweng.finance.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.domain.dto.FinanceProductCreateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductPageRequest;
import com.dafuweng.finance.domain.dto.FinanceProductUpdateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductVO;
import com.dafuweng.finance.entity.FinanceProduct;

import java.util.List;

public interface FinanceProductService {

    Long create(FinanceProductCreateRequest request);

    void update(FinanceProductUpdateRequest request);

    void delete(Long id);

    FinanceProductVO getById(Long id);

    List<FinanceProductVO> list();

    PageResult<FinanceProductVO> page(FinanceProductPageRequest request);

    FinanceProduct getEntityById(Long id);
}
