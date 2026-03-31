package com.dafuweng.common.core.enums;

import lombok.Getter;

/**
 * 全局错误码枚举
 *
 * 【强制】错误码区间分配：
 * 1xxxxx — 认证模块
 * 2xxxxx — 销售模块（客户/洽谈/合同）
 * 3xxxxx — 金融模块（产品/审核/服务费）
 * 4xxxxx — 系统模块（部门/账号/参数）
 * 5xxxxx — 公共模块（参数校验/文件/基础设施）
 * 9xxxxx — 全局系统级错误
 */
@Getter
public enum ErrorCode {

    // ================================================================
    // 全局级错误码 (9xxxxx)
    // ================================================================
    SYSTEM_ERROR(900001, "系统异常，请稍后重试"),
    PARAM_VALID_ERROR(900002, "参数校验失败"),
    DATA_NOT_FOUND(900003, "数据不存在"),
    DATA_CONFLICT(900004, "数据冲突"),
    OPERATION_FORBIDDEN(900005, "操作被禁止"),
    TOKEN_EXPIRED(900101, "登录已过期，请重新登录"),
    TOKEN_INVALID(900102, "Token无效"),
    TOKEN_BLACKLISTED(900103, "账号已被退出登录"),

    // ================================================================
    // 认证模块错误码 (1xxxxx)
    // ================================================================
    AUTH_LOGIN_FAILED(100001, "用户名或密码错误"),
    AUTH_ACCOUNT_DISABLED(100002, "账号已被禁用"),
    AUTH_ACCOUNT_LOCKED(100003, "账号已被锁定，请%d分钟后重试"),
    AUTH_PERMISSION_DENIED(100004, "无访问权限"),
    AUTH_CAPTCHA_ERROR(100005, "验证码错误"),
    AUTH_CAPTCHA_EXPIRED(100006, "验证码已过期"),
    AUTH_REFRESH_TOKEN_FAILED(100007, "刷新Token失败"),

    // ================================================================
    // 销售模块错误码 (2xxxxx)
    // ================================================================

    // 客户 (200xxx)
    CUSTOMER_DUPLICATE(200001, "该客户已存在（姓名+手机号重复）"),
    CUSTOMER_NOT_FOUND(200002, "客户不存在"),
    CUSTOMER_TRANSFER_FORBIDDEN(200003, "无权限转移该客户"),
    CUSTOMER_CLAIM_FORBIDDEN(200004, "该客户不在公海中，无法领取"),
    CUSTOMER_CLAIM_LOCKED(200005, "客户正在被其他操作处理，请稍后"),
    CUSTOMER_STATUS_ERROR(200006, "客户状态不允许此操作"),

    // 洽谈 (201xxx)
    CONTACT_CUSTOMER_NOT_MATCH(201001, "洽谈记录与客户不匹配"),
    CONTACT_CUSTOMER_ERROR(201002, "无法添加洽谈记录"),

    // 合同 (202xxx)
    CONTRACT_DUPLICATE(202001, "合同编号已存在"),
    CONTRACT_NOT_FOUND(202002, "合同不存在"),
    CONTRACT_STATUS_ERROR(202003, "合同状态不允许此操作"),
    CONTRACT_AMOUNT_ERROR(202004, "合同金额必须大于0"),
    CONTRACT_SIGN_DATE_ERROR(202005, "签署日期不能晚于当前日期"),
    CONTRACT_ATTACHMENT_ERROR(202006, "合同附件上传失败"),
    CONTRACT_SEND_FINANCE_ERROR(202007, "该合同状态不允许发送至金融部"),

    // 工作日志 (203xxx)
    WORKLOG_DUPLICATE(203001, "当日工作日志已存在"),
    WORKLOG_NOT_FOUND(203002, "工作日志不存在"),

    // 业绩 (204xxx)
    PERFORMANCE_DUPLICATE(204001, "该合同已存在业绩记录"),
    PERFORMANCE_NOT_FOUND(204002, "业绩记录不存在"),
    PERFORMANCE_CALCULATE_LOCKED(204003, "业绩正在计算中，请勿重复操作"),

    // ================================================================
    // 金融模块错误码 (3xxxxx)
    // ================================================================

    // 产品 (300xxx)
    PRODUCT_NOT_FOUND(300001, "金融产品不存在"),
    PRODUCT_OFFLINE(300002, "该产品已下架"),
    PRODUCT_AMOUNT_ERROR(300003, "贷款金额超出产品范围"),
    PRODUCT_TERM_ERROR(300004, "贷款期限超出产品范围"),

    // 审核 (301xxx)
    AUDIT_NOT_FOUND(301001, "贷款审核记录不存在"),
    AUDIT_STATUS_ERROR(301002, "审核状态不允许此操作"),
    AUDIT_BANK_NOT_SELECTED(301003, "请先选择银行"),
    AUDIT_PRODUCT_NOT_SELECTED(301004, "请先推荐金融产品"),
    AUDIT_SUBMIT_BANK_ERROR(301005, "当前状态不允许提交银行"),
    AUDIT_BANK_RESULT_ERROR(301006, "银行结果录入失败"),
    AUDIT_APPROVE_LOCKED(301007, "审核正在处理中，请勿重复提交"),

    // 服务费 (302xxx)
    SERVICEFEE_CONTRACT_ERROR(302001, "合同不存在或未签署"),
    SERVICEFEE_ALREADY_PAID(302002, "该服务费已支付"),
    SERVICEFEE_AMOUNT_ERROR(302003, "实收金额不能大于应收金额"),

    // 银行 (303xxx)
    BANK_NOT_FOUND(303001, "合作银行不存在"),
    BANK_DISABLED(303002, "该银行已暂停合作"),

    // ================================================================
    // 系统模块错误码 (4xxxxx)
    // ================================================================

    // 部门 (400xxx)
    DEPT_NOT_FOUND(400001, "部门不存在"),
    DEPT_EXISTS_CHILDREN(400002, "该部门存在下级部门，无法删除"),
    DEPT_EXISTS_USERS(400003, "该部门存在用户，无法删除"),
    DEPT_PARENT_ERROR(400004, "父部门不存在"),

    // 账号 (401xxx)
    ACCOUNT_NOT_FOUND(401001, "账号不存在"),
    ACCOUNT_USERNAME_EXISTS(401002, "用户名已存在"),
    ACCOUNT_PHONE_EXISTS(401003, "手机号已被使用"),
    ACCOUNT_OLD_PASSWORD_ERROR(401004, "原密码错误"),

    // 战区 (402xxx)
    ZONE_NOT_FOUND(402001, "战区不存在"),

    // 参数 (403xxx)
    PARAM_NOT_FOUND(403001, "系统参数不存在"),
    PARAM_KEY_EXISTS(403002, "参数键已存在"),
    PARAM_VALUE_ERROR(403003, "参数值格式错误"),

    // ================================================================
    // 公共模块错误码 (5xxxxx)
    // ================================================================
    FILE_UPLOAD_ERROR(500001, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(500002, "不允许的文件类型"),
    FILE_SIZE_EXCEED(500003, "文件大小超出限制（最大%dMB）"),
    FILE_NOT_FOUND(500004, "文件不存在"),
    ENUM_VALUE_ERROR(500005, "枚举值%d不合法，合法值为：%s"),
    DATE_RANGE_ERROR(500006, "日期范围不合法，结束日期不能早于开始日期"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 格式化消息（用于占位符如 %d）
     */
    public String formatMessage(Object... args) {
        if (args == null || args.length == 0) {
            return this.message;
        }
        return String.format(this.message, args);
    }
}
