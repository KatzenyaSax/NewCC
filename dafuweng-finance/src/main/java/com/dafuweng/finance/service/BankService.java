package com.dafuweng.finance.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.domain.dto.BankPageRequest;
import com.dafuweng.finance.domain.dto.BankVO;
import com.dafuweng.finance.entity.Bank;

import java.util.List;

public interface BankService {

    BankVO getById(Long id);

    List<BankVO> list();

    PageResult<BankVO> page(BankPageRequest request);

    Bank getEntityById(Long id);
}
