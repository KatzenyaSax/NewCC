package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AccountCreateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 50, message = "用户名长度4-50位")
    private String username;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名最多50字符")
    private String realName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱最多100字符")
    private String email;

    @NotNull(message = "部门ID不能为空")
    private Long deptId;

    private Long zoneId;

    @NotEmpty(message = "至少选择一个角色")
    private List<Long> roleIds;
}
