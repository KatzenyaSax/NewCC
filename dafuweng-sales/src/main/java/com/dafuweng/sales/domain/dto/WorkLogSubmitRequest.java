package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkLogSubmitRequest {

    /** 日志日期 */
    @NotNull(message = "日志日期不能为空")
    private LocalDate logDate;

    /** 打电话数 */
    @NotNull(message = "打电话数不能为空")
    @Min(value = 0, message = "打电话数不能为负")
    private Integer callsMade;

    /** 有效电话数 */
    @NotNull(message = "有效电话数不能为空")
    @Min(value = 0, message = "有效电话数不能为负")
    private Integer effectiveCalls;

    /** 意向客户数 */
    @NotNull(message = "意向客户数不能为空")
    @Min(value = 0, message = "意向客户数不能为负")
    private Integer intentionClients;

    /** 面谈客户数 */
    @NotNull(message = "面谈客户数不能为空")
    @Min(value = 0, message = "面谈客户数不能为负")
    private Integer faceToFaceClients;

    /** 备注内容 */
    private String content;
}
