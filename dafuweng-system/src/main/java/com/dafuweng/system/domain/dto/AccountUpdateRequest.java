package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AccountUpdateRequest {

    @NotNull(message = "账号ID不能为空")
    private Long id;

    @Size(max = 50, message = "真实姓名最多50字符")
    private String realName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱最多100字符")
    private String email;

    private Long deptId;

    private Long zoneId;

    private List<Long> roleIds;

    private Integer status;
}
