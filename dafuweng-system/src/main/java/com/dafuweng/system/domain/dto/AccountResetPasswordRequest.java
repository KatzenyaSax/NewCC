package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountResetPasswordRequest {

    @NotNull(message = "账号ID不能为空")
    private Long id;
}
