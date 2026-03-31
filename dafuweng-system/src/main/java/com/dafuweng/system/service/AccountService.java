package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysAccount;

public interface AccountService {

    Long create(AccountCreateRequest request);

    void update(AccountUpdateRequest request);

    void delete(Long id);

    SysAccount getById(Long id);

    void resetPassword(AccountResetPasswordRequest request);

    void changePassword(AccountChangePasswordRequest request);

    PageResult<AccountVO> page(AccountPageRequest request);
}
