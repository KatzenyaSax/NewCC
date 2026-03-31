# RESTful API 规范说明书 — 大富翁金融服务公司贷款管理系统

**版本:** v1.0
**日期:** 2026-03-31
**状态:** 草稿
**依据:** 阿里巴巴《Java开发手册》+ 数据库表结构 (database.sql)

---

## 1. 概述

本文档定义符合阿里巴巴《Java开发手册》强制规范的 RESTful API 设计标准，覆盖：

- 全局统一返回结构 (`R<T>` / `PageResult<T>`)
- 错误码规范体系
- DTO/VO/Converter 分层架构
- 各微服务核心接口定义


**设计原则：**
1. **外部接口稳定，内部模型灵活** — DTO 是对外契约，Entity 是内部实现
2. **零字符串拼接 SQL** — 全部走 MyBatisPlus 参数化查询
3. **分层职责清晰** — Controller → Service → Mapper，DTO/VO 不穿透三层
4. **枚举替代魔法值** — 所有状态/类型使用 Java 枚举

---

## 2. 全局统一返回类

### 2.1 R<T> 统一响应结构

位置：`dafuweng-common/common-core/.../result/R.java`

```java
package com.dafuweng.common.core.result;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 全局统一返回结构
 *
 * 阿里巴巴《Java开发手册》强制规范：
 * 1. 【强制】正例返回必须使用本类的 ok()/error() 静态方法，禁止直接 new
 * 2. 【强制】message 字段禁止返回前端敏感信息（异常堆栈/ SQL报错/内部路径）
 * 3. 【强制】data 字段为 null 时必须序列化为 null，不能序列化为空字符串""
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 业务状态码 */
    private int code;

    /** 描述信息（可前端展示） */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳（毫秒） */
    private long timestamp;

    /** traceId（链路追踪） */
    private String traceId;

    // ================================================================
    // 静态工厂方法（强制使用，禁止直接 new R<>）
    // ================================================================

    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return build(SUCCESS_CODE, "success", data);
    }

    public static <T> R<T> ok(T data, String message) {
        return build(SUCCESS_CODE, message, data);
    }

    public static <T> R<T> error(int code, String message) {
        return build(code, message, null);
    }

    public static <T> R<T> error(ErrorCode errorCode) {
        return build(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> R<T> error(int code, String message, T data) {
        return build(code, message, data);
    }

    public static <T> R<T> build(int code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    // ================================================================
    // 快捷判断方法
    // ================================================================

    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }

    public boolean isError() {
        return this.code != SUCCESS_CODE;
    }

    // ================================================================
    // Builder 模式（可选，用于链式构造复杂响应）
    // ================================================================

    public static <T> RBuilder<T> builder() {
        return new RBuilder<>();
    }

    public static class RBuilder<T> {
        private int code = SUCCESS_CODE;
        private String message = "success";
        private T data;
        private String traceId;

        public RBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public RBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public RBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public RBuilder<T> traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public R<T> build() {
            R<T> r = new R<>();
            r.setCode(this.code);
            r.setMessage(this.message);
            r.setData(this.data);
            r.setTraceId(this.traceId);
            r.setTimestamp(System.currentTimeMillis());
            return r;
        }
    }
}
```

### 2.2 PageResult<T> 分页响应结构

位置：`dafuweng-common/common-core/.../result/PageResult.java`

```java
package com.dafuweng.common.core.result;

import cn.hutool.core.lang.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页统一响应结构
 *
 * 【强制】分页响应必须使用本类，不允许返回裸 List 并自行拼接分页字段
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 当前页数据列表 */
    private List<T> records;

    /** 总记录数 */
    private long total;

    /** 每页条数 */
    private long size;

    /** 当前页码 */
    private long current;

    /** 总页数 */
    private long pages;

    /** 是否有下一页 */
    private boolean hasNext;

    /** 是否有上一页 */
    private boolean hasPrevious;

    // ================================================================
    // 构造方法
    // ================================================================

    public PageResult() {}

    public PageResult(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = size > 0 ? (total + size - 1) / size : 0;
        this.hasNext = current < pages;
        this.hasPrevious = current > 1;
    }

    // ================================================================
    // 静态工厂方法
    // ================================================================

    /**
     * 从 MyBatisPlus IPage 转换
     */
    public static <T> PageResult<T> of(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        return new PageResult<>(
            page.getRecords(),
            page.getTotal(),
            page.getSize(),
            page.getCurrent()
        );
    }

    /**
     * 构造空分页结果
     */
    public static <T> PageResult<T> empty(long size) {
        return new PageResult<>(List.of(), 0, size, 1);
    }
}
```

---

## 3. 错误码规范

### 3.1 错误码定义（ErrorCode 枚举）

位置：`dafuweng-common/common-core/.../enums/ErrorCode.java`

```java
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
```

---

## 4. DTO/VO/Converter 分层架构

### 4.1 分层概述

```
┌─────────────────────────────────────────────────────────────────┐
│                        Controller 层                            │
│   接收 HTTP Request，返回 R<T>/R<PageResult<T>>                 │
└─────────────────────────┬───────────────────────────────────────┘
                          │ Request DTO / Response VO
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                         Service 层                              │
│   业务逻辑处理，Entity ↔ DTO/VO 转换（调用 Converter）           │
└─────────────────────────┬───────────────────────────────────────┘
                          │ Entity
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                       Mapper/DAO 层                              │
│   MyBatisPlus CRUD，数据表映射 Entity                            │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 分层规范（阿里巴巴《Java开发手册》）

| 规范 | 说明 |
|------|------|
| **【强制】DTO 不穿透三层** | DTO 只出现在 Controller 和 Service 之间，禁止穿透到 Mapper |
| **【强制】VO 只返回给前端** | VO（View Object）仅用于接口返回，禁止用于内部逻辑判断 |
| **【强制】Entity 禁止直接返回** | 任何接口禁止返回裸露的 Entity，必须经 DTO/VO 转换 |
| **【强制】MapStruct 优先** | Entity ↔ DTO 转换统一使用 MapStruct，禁止手写 setter/getter 转换 |
| **【强制】不接受实体作为请求参数** | Controller 方法参数必须用 DTO，禁止 @RequestBody Entity |

### 4.3 Converter 接口定义（MapStruct）

位置：`dafuweng-common/common-core/.../converter/EntityConverter.java`

```java
package com.dafuweng.common.core.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 实体转换器基接口
 *
 * 所有 Entity ↔ DTO/VO 转换器必须继承本接口
 *
 * 【强制】使用 MapStruct，编译时自动生成转换代码，零运行时开销
 * 【强制】转换器命名规范：源实体+To+目标对象，如 CustomerToDTOConverter
 *
 * @param <S> Source 源实体类型
 * @param <T> Target 目标类型（DTO/VO）
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntityConverter<S, T> {

    /**
     * 单个实体转换
     */
    T convert(S source);

    /**
     * 实体复制（同名属性拷贝）
     */
    S copy(S source, @MappingTarget S target);

    /**
     * 实体列表转换
     */
    List<T> convert(List<S> sources);

    /**
     * 空安全转换（source 为 null 时返回 null，不抛异常）
     */
    default T convertSafe(S source) {
        return source == null ? null : convert(source);
    }
}
```

---

## 5. 各微服务 DTO/VO 定义

### 5.1 auth-service（认证服务）

#### 5.1.1 DTO 定义

位置：`dafuweng-auth/src/main/java/.../auth/domain/dto/`

```java
// ================================================================
// 登录相关
// ================================================================

@Data
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 50, message = "用户名长度4-50位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度6-100位")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度4-6位")
    private String captchaCode;

    @NotBlank(message = "验证码key不能为空")
    private String captchaKey;
}

@Data
public class LoginResponse {
    /** JWT Token */
    private String accessToken;

    /** 刷新 Token */
    private String refreshToken;

    /** Token 类型 */
    private String tokenType = "Bearer";

    /** 过期时间（秒） */
    private long expiresIn;

    /** 用户信息 */
    private UserVO userInfo;
}

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "刷新Token不能为空")
    private String refreshToken;
}

@Data
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}

// ================================================================
// 用户信息
// ================================================================

@Data
public class UserVO {
    private Long userId;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private Integer status;
    private List<String> roles;
    private List<String> permissions;
}

@Data
public class UserPageRequest {
    private String username;
    private String realName;
    private Long deptId;
    private Long zoneId;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
```

#### 5.1.2 Converter

```java
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysUserConverter extends EntityConverter<SysUser, UserVO> {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "deptName", source = "dept.deptName")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    UserVO toVO(SysUser entity);

    List<UserVO> toVOList(List<SysUser> entities);
}
```

---

### 5.2 sales-service（销售服务）

#### 5.2.1 客户管理 DTO/VO

位置：`dafuweng-sales/src/main/java/.../sales/domain/dto/`

```java
// ================================================================
// 客户相关 DTO
// ================================================================

/**
 * 客户创建请求
 */
@Data
public class CustomerCreateRequest {

    @NotBlank(message = "客户姓名不能为空")
    @Size(max = 100, message = "客户姓名最多100字符")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 20, message = "身份证号最多20字符")
    private String idCard;

    /** 客户类型：1-个人 2-企业 */
    @NotNull(message = "客户类型不能为空")
    @Min(value = 1, message = "客户类型不合法")
    @Max(value = 2, message = "客户类型不合法")
    private Integer customerType;

    /** 企业客户必填 */
    @Size(max = 200, message = "企业名称最多200字符")
    private String companyName;

    @Size(max = 100, message = "企业法人最多100字符")
    private String companyLegalPerson;

    /** 意向等级：1-A 2-B 3-C 4-D */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionLevel;

    @DecimalMin(value = "0", message = "意向金额不能为负")
    private BigDecimal loanIntentionAmount;

    @Size(max = 100, message = "意向产品最多100字符")
    private String loanIntentionProduct;

    /** 客户来源 */
    @Size(max = 50, message = "客户来源最多50字符")
    private String source;

    /** 客户来源 */
    @Size(max = 50, message = "客户来源最多50字符")
    private String source;
}

/**
 * 客户更新请求
 */
@Data
public class CustomerUpdateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long id;

    @Size(max = 100, message = "客户姓名最多100字符")
    private String name;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 20, message = "身份证号最多20字符")
    private String idCard;

    @Min(value = 1, message = "客户类型不合法")
    @Max(value = 2, message = "客户类型不合法")
    private Integer customerType;

    @Size(max = 200, message = "企业名称最多200字符")
    private String companyName;

    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionLevel;

    /** 下次跟进日期 */
    private LocalDate nextFollowUpDate;

    @DecimalMin(value = "0", message = "意向金额不能为负")
    private BigDecimal loanIntentionAmount;

    @Size(max = 100, message = "意向产品最多100字符")
    private String loanIntentionProduct;
}

/**
 * 客户查重请求
 */
@Data
public class CustomerDuplicateCheckRequest {
    @NotBlank(message = "客户姓名不能为空")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}

/**
 * 客户查重响应
 */
@Data
public class CustomerDuplicateCheckResponse {
    /** 是否存在 */
    private boolean duplicate;

    /** 已存在的客户ID */
    private Long existCustomerId;

    /** 已存在的客户姓名 */
    private String existCustomerName;

    /** 已录入的销售姓名 */
    private String existSalesRepName;

    /** 录入时间 */
    private LocalDateTime existCreatedAt;
}

/**
 * 客户迁移请求
 */
@Data
public class CustomerTransferRequest {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotNull(message = "目标销售ID不能为空")
    private Long toSalesRepId;

    @NotBlank(message = "转移原因不能为空")
    @Size(max = 200, message = "转移原因最多200字符")
    private String reason;
}

/**
 * 客户批注请求
 */
@Data
public class CustomerAnnotateRequest {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotBlank(message = "批注内容不能为空")
    @Size(max = 500, message = "批注内容最多500字符")
    private String content;
}

/**
 * 客户分页查询请求
 */
@Data
public class CustomerPageRequest {
    private String name;
    private String phone;
    private Integer customerType;
    private Integer status;
    private Integer intentionLevel;
    private Long salesRepId;
    private Long deptId;
    private Long zoneId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}

// ================================================================
// 客户 VO
// ================================================================

@Data
public class CustomerVO {
    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private Integer customerType;
    private String customerTypeDesc;
    private String companyName;
    private String companyLegalPerson;
    private BigDecimal companyRegCapital;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private Integer intentionLevel;
    private String intentionLevelDesc;
    private Integer status;
    private String statusDesc;
    private LocalDateTime lastContactDate;
    private LocalDate nextFollowUpDate;
    private LocalDateTime publicSeaTime;
    private String publicSeaReason;
    private List<AnnotationVO> annotations;
    private String source;
    private BigDecimal loanIntentionAmount;
    private String loanIntentionProduct;
    private LocalDateTime createdAt;
    private String createdByName;
    private LocalDateTime updatedAt;
}

@Data
public class AnnotationVO {
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime time;
}

@Data
public class CustomerPublicSeaVO {
    private Long id;
    private String name;
    private String phone;
    private Integer customerType;
    private String customerTypeDesc;
    private Integer intentionLevel;
    private String intentionLevelDesc;
    private LocalDateTime publicSeaTime;
    private String publicSeaReason;
    private String source;
    private BigDecimal loanIntentionAmount;
    private String loanIntentionProduct;
    private LocalDateTime createdAt;
    private String salesRepName;
    private String deptName;
}
```

#### 5.2.2 洽谈记录 DTO/VO

```java
// ================================================================
// 洽谈记录 DTO
// ================================================================

@Data
public class ContactRecordCreateRequest {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /** 联系类型：1-电话 2-面谈 3-转介绍 */
    @NotNull(message = "联系类型不能为空")
    @Min(value = 1, message = "联系类型不合法")
    @Max(value = 3, message = "联系类型不合法")
    private Integer contactType;

    @NotNull(message = "联系时间不能为空")
    private LocalDateTime contactDate;

    @NotBlank(message = "洽谈内容不能为空")
    @Size(max = 2000, message = "洽谈内容最多2000字符")
    private String content;

    /** 洽谈前意向等级 */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionBefore;

    /** 洽谈后意向等级 */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionAfter;

    /** 下次跟进日期 */
    private LocalDate followUpDate;

    /** 附件URLs（JSON数组） */
    private List<String> attachmentUrls;
}

@Data
public class ContactRecordVO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long salesRepId;
    private String salesRepName;
    private Integer contactType;
    private String contactTypeDesc;
    private LocalDateTime contactDate;
    private String content;
    private Integer intentionBefore;
    private String intentionBeforeDesc;
    private Integer intentionAfter;
    private String intentionAfterDesc;
    private LocalDate followUpDate;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt;
}
```

#### 5.2.3 合同管理 DTO/VO

```java
// ================================================================
// 合同 DTO
// ================================================================

@Data
public class ContractCreateRequest {
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotNull(message = "合同金额不能为空")
    @DecimalMin(value = "0.01", message = "合同金额必须大于0")
    private BigDecimal contractAmount;

    @NotNull(message = "服务费率不能为空")
    @DecimalMin(value = "0", message = "服务费率不能为负")
    @DecimalMax(value = "1", message = "服务费率不能大于1")
    private BigDecimal serviceFeeRate;

    /** 贷款用途 */
    @Size(max = 200, message = "贷款用途最多200字符")
    private String loanUse;

    /** 担保信息（JSON） */
    private String guaranteeInfo;

    @Size(max = 500, message = "备注最多500字符")
    private String remark;
}

@Data
public class ContractSignRequest {
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 纸质合同编号 */
    @NotBlank(message = "纸质合同编号不能为空")
    @Size(max = 100, message = "纸质合同编号最多100字符")
    private String paperContractNo;

    /** 签署日期 */
    @NotNull(message = "签署日期不能为空")
    private LocalDate signDate;

    /** 附件列表 */
    @NotEmpty(message = "至少需要上传一个附件")
    private List<ContractAttachmentRequest> attachments;
}

@Data
public class ContractAttachmentRequest {
    @NotBlank(message = "附件类型不能为空")
    @Pattern(regexp = "^(business_license|id_card|other)$", message = "附件类型不合法")
    private String attachmentType;

    @NotBlank(message = "文件URL不能为空")
    @Size(max = 500, message = "文件URL最多500字符")
    private String fileUrl;

    @NotBlank(message = "文件名不能为空")
    @Size(max = 200, message = "文件名最多200字符")
    private String fileName;

    private Long fileSize;

    @Size(max = 32, message = "文件MD5最多32字符")
    private String fileMd5;
}

@Data
public class ContractPayFirstRequest {
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 首期支付日期 */
    @NotNull(message = "支付日期不能为空")
    private LocalDate payDate;
}

@Data
public class ContractSendFinanceRequest {
    @NotNull(message = "合同ID不能为空")
    private Long contractId;
}

@Data
public class ContractPageRequest {
    private String contractNo;
    private Long customerId;
    private String customerName;
    private Long salesRepId;
    private Long deptId;
    private Long productId;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}

// ================================================================
// 合同 VO
// ================================================================

@Data
public class ContractVO {
    private Long id;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long productId;
    private String productName;
    private BigDecimal contractAmount;
    private BigDecimal actualLoanAmount;
    private BigDecimal serviceFeeRate;
    private BigDecimal serviceFee1;
    private BigDecimal serviceFee2;
    private Boolean serviceFee1Paid;
    private LocalDate serviceFee1PayDate;
    private Boolean serviceFee2Paid;
    private LocalDate serviceFee2PayDate;
    private Integer status;
    private String statusDesc;
    private LocalDate signDate;
    private String paperContractNo;
    private LocalDateTime financeSendTime;
    private LocalDateTime financeReceiveTime;
    private String loanUse;
    private String guaranteeInfo;
    private String rejectReason;
    private String remark;
    private List<ContractAttachmentVO> attachments;
    private LocalDateTime createdAt;
    private String createdByName;
    private LocalDateTime updatedAt;
}

@Data
public class ContractAttachmentVO {
    private Long id;
    private Long contractId;
    private String attachmentType;
    private String attachmentTypeDesc;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private LocalDateTime uploadTime;
    private String uploadByName;
}

@Data
public class ContractDetailVO extends ContractVO {
    private CustomerVO customer;
    private LoanAuditVO loanAudit;
}
```

#### 5.2.4 业绩 DTO/VO

```java
// ================================================================
// 业绩 DTO/VO
// ================================================================

@Data
public class PerformancePageRequest {
    private Long salesRepId;
    private Long deptId;
    private Long zoneId;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}

@Data
public class PerformanceVO {
    private Long id;
    private Long contractId;
    private String contractNo;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private BigDecimal contractAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private Integer status;
    private String statusDesc;
    private LocalDateTime calculateTime;
    private LocalDateTime confirmTime;
    private LocalDateTime grantTime;
    private String remark;
    private LocalDateTime createdAt;
}

@Data
public class PerformanceRankingVO {
    private Integer rank;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private BigDecimal totalContractAmount;
    private BigDecimal totalCommission;
    private Integer contractCount;
}

@Data
public class PerformanceAnalysisVO {
    private BigDecimal totalContractAmount;
    private BigDecimal totalCommission;
    private Integer totalContractCount;
    private BigDecimal avgContractAmount;
    private BigDecimal avgCommissionRate;
    private Map<Integer, BigDecimal> monthlyAmount;
    private Map<Integer, BigDecimal> monthlyCommission;
}
```

---

### 5.3 finance-service（金融服务）

#### 5.3.1 金融产品 DTO/VO

```java
// ================================================================
// 金融产品 DTO
// ================================================================

@Data
public class FinanceProductCreateRequest {
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称最多100字符")
    private String productName;

    @NotNull(message = "所属银行ID不能为空")
    private Long bankId;

    @NotNull(message = "最小贷款金额不能为空")
    @DecimalMin(value = "0.01", message = "最小贷款金额必须大于0")
    private BigDecimal minAmount;

    @NotNull(message = "最大贷款金额不能为空")
    @DecimalMin(value = "0.01", message = "最大贷款金额必须大于0")
    private BigDecimal maxAmount;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal interestRate;

    @NotNull(message = "最小期限不能为空")
    @Min(value = 1, message = "最小期限最少1个月")
    private Integer minTerm;

    @NotNull(message = "最大期限不能为空")
    @Min(value = 1, message = "最大期限最少1个月")
    private Integer maxTerm;

    /** 申请条件（JSON数组） */
    private List<String> requirements;

    /** 所需材料（JSON数组） */
    private List<String> documents;

    /** 产品特点 */
    private String productFeatures;

    @DecimalMin(value = "0", message = "渠道佣金比例不能为负")
    @DecimalMax(value = "1", message = "渠道佣金比例不能大于1")
    private BigDecimal commissionRate;
}

@Data
public class FinanceProductUpdateRequest {
    @NotNull(message = "产品ID不能为空")
    private Long id;

    @Size(max = 100, message = "产品名称最多100字符")
    private String productName;

    @DecimalMin(value = "0.01", message = "最小贷款金额必须大于0")
    private BigDecimal minAmount;

    @DecimalMin(value = "0.01", message = "最大贷款金额必须大于0")
    private BigDecimal maxAmount;

    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal interestRate;

    @Min(value = 1, message = "最小期限最少1个月")
    private Integer minTerm;

    @Min(value = 1, message = "最大期限最少1个月")
    private Integer maxTerm;

    private List<String> requirements;
    private List<String> documents;
    private String productFeatures;

    @DecimalMin(value = "0", message = "渠道佣金比例不能为负")
    @DecimalMax(value = "1", message = "渠道佣金比例不能大于1")
    private BigDecimal commissionRate;

    /** 状态：1-上架 0-下架 */
    private Integer status;
}

@Data
public class FinanceProductPageRequest {
    private String productName;
    private Long bankId;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private Integer term;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}

// ================================================================
// 金融产品 VO
// ================================================================

@Data
public class FinanceProductVO {
    private Long id;
    private String productCode;
    private String productName;
    private Long bankId;
    private String bankName;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String amountRange;
    private BigDecimal interestRate;
    private String interestRateDesc;
    private Integer minTerm;
    private Integer maxTerm;
    private String termRange;
    private List<String> requirements;
    private List<String> documents;
    private String productFeatures;
    private BigDecimal commissionRate;
    private Integer status;
    private String statusDesc;
    private LocalDateTime onlineTime;
    private LocalDateTime offlineTime;
    private LocalDateTime createdAt;
}

@Data
public class FinanceProductSimpleVO {
    private Long id;
    private String productName;
    private String bankName;
    private String amountRange;
    private String interestRateDesc;
    private String termRange;
}
```

#### 5.3.2 贷款审核 DTO/VO

```java
// ================================================================
// 贷款审核 DTO
// ================================================================

@Data
public class LoanAuditReceiveRequest {
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 备注 */
    private String remark;
}

@Data
public class LoanAuditReviewRequest {
    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotNull(message = "推荐产品ID不能为空")
    private Long recommendedProductId;

    @NotNull(message = "审批金额不能为空")
    @DecimalMin(value = "0.01", message = "审批金额必须大于0")
    private BigDecimal approvedAmount;

    @NotNull(message = "审批期限不能为空")
    @Min(value = 1, message = "审批期限最少1个月")
    private Integer approvedTerm;

    @NotNull(message = "审批利率不能为空")
    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal approvedInterestRate;

    /** 审核意见 */
    @Size(max = 500, message = "审核意见最多500字符")
    private String auditOpinion;

    /** 附件URLs */
    private List<String> attachmentUrls;
}

@Data
public class LoanAuditSubmitBankRequest {
    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotNull(message = "银行ID不能为空")
    private Long bankId;

    /** 提交备注 */
    private String remark;
}

@Data
public class LoanAuditBankResultRequest {
    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    /** 银行审核状态：approved/rejected */
    @NotBlank(message = "银行审核状态不能为空")
    @Pattern(regexp = "^(approved|rejected)$", message = "银行审核状态不合法")
    private String bankAuditStatus;

    /** 银行反馈内容 */
    private String bankFeedbackContent;

    /** 实际放款金额（银行批准时必填） */
    @DecimalMin(value = "0", message = "放款金额不能为负")
    private BigDecimal actualLoanAmount;

    /** 实际执行利率 */
    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal actualInterestRate;

    /** 银行放款日期 */
    private LocalDate loanGrantedDate;
}

@Data
public class LoanAuditRejectRequest {
    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotBlank(message = "拒绝原因不能为空")
    @Size(max = 500, message = "拒绝原因最多500字符")
    private String rejectReason;
}

// ================================================================
// 贷款审核 VO
// ================================================================

@Data
public class LoanAuditVO {
    private Long id;
    private Long contractId;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private BigDecimal contractAmount;
    private Long financeSpecialistId;
    private String financeSpecialistName;
    private Long recommendedProductId;
    private String recommendedProductName;
    private BigDecimal approvedAmount;
    private Integer approvedTerm;
    private BigDecimal approvedInterestRate;
    private Integer auditStatus;
    private String auditStatusDesc;
    private Long bankId;
    private String bankName;
    private String bankAuditStatus;
    private String bankAuditStatusDesc;
    private LocalDateTime bankApplyTime;
    private LocalDateTime bankFeedbackTime;
    private String bankFeedbackContent;
    private String rejectReason;
    private String auditOpinion;
    private LocalDateTime auditDate;
    private LocalDateTime loanGrantedDate;
    private BigDecimal actualLoanAmount;
    private BigDecimal actualInterestRate;
    private LocalDateTime createdAt;
    private List<LoanAuditRecordVO> auditRecords;
}

@Data
public class LoanAuditRecordVO {
    private Long id;
    private Long loanAuditId;
    private Long operatorId;
    private String operatorName;
    private String operatorRole;
    private String action;
    private String actionDesc;
    private String content;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt;
}

@Data
public class LoanAuditSimpleVO {
    private Long id;
    private String contractNo;
    private String customerName;
    private String customerPhone;
    private BigDecimal contractAmount;
    private Integer auditStatus;
    private String auditStatusDesc;
    private LocalDateTime createdAt;
}
```

#### 5.3.3 服务费 DTO/VO

```java
// ================================================================
// 服务费 DTO
// ================================================================

@Data
public class ServiceFeeRecordCreateRequest {
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 费用类型：1-首期服务费 2-二期服务费 */
    @NotNull(message = "费用类型不能为空")
    @Min(value = 1, message = "费用类型不合法")
    @Max(value = 2, message = "费用类型不合法")
    private Integer feeType;

    @NotNull(message = "实收金额不能为空")
    @DecimalMin(value = "0", message = "实收金额不能为负")
    private BigDecimal amount;

    /** 应收金额（前端计算后传回） */
    @NotNull(message = "应收金额不能为空")
    @DecimalMin(value = "0", message = "应收金额不能为负")
    private BigDecimal shouldAmount;

    /** 支付方式 */
    @Pattern(regexp = "^(bank_transfer|wechat|alipay|cash)$", message = "支付方式不合法")
    private String paymentMethod;

    /** 支付日期 */
    private LocalDate paymentDate;

    /** 付款账户 */
    @Size(max = 100, message = "付款账户最多100字符")
    private String paymentAccount;

    /** 收据编号 */
    @Size(max = 50, message = "收据编号最多50字符")
    private String receiptNo;

    @Size(max = 500, message = "备注最多500字符")
    private String remark;
}

@Data
public class ServiceFeeRecordVO {
    private Long id;
    private Long contractId;
    private String contractNo;
    private String customerName;
    private Integer feeType;
    private String feeTypeDesc;
    private BigDecimal amount;
    private BigDecimal shouldAmount;
    private String paymentMethod;
    private String paymentMethodDesc;
    private Integer paymentStatus;
    private String paymentStatusDesc;
    private LocalDate paymentDate;
    private String paymentAccount;
    private String receiptNo;
    private Long accountantId;
    private String accountantName;
    private String remark;
    private LocalDateTime createdAt;
}

@Data
public class ServiceFeeRecordPageRequest {
    private Long contractId;
    private Integer feeType;
    private Integer paymentStatus;
    private Long accountantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
```

---

### 5.4 system-service（系统管理）

#### 5.4.1 部门管理 DTO/VO

```java
// ================================================================
// 部门 DTO
// ================================================================

@Data
public class DepartmentCreateRequest {
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称最多100字符")
    private String deptName;

    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码最多50字符")
    private String deptCode;

    /** 父部门ID：0-根部门 */
    @NotNull(message = "父部门ID不能为空")
    private Long parentId;

    @NotNull(message = "战区ID不能为空")
    private Long zoneId;

    /** 部门经理ID */
    private Long managerId;

    private Integer sortOrder;
}

@Data
public class DepartmentUpdateRequest {
    @NotNull(message = "部门ID不能为空")
    private Long id;

    @Size(max = 100, message = "部门名称最多100字符")
    private String deptName;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Long managerId;
    private Integer sortOrder;
}

@Data
public class DepartmentVO {
    private Long id;
    private String deptCode;
    private String deptName;
    private Long parentId;
    private String parentName;
    private Long zoneId;
    private String zoneName;
    private Long managerId;
    private String managerName;
    private Integer sortOrder;
    private Integer status;
    private String statusDesc;
    private List<DepartmentVO> children;
    private LocalDateTime createdAt;
}

@Data
public class DepartmentTreeVO {
    private Long id;
    private String deptName;
    private Long parentId;
    private Long zoneId;
    private String zoneName;
    private List<DepartmentTreeVO> children;
}
```

#### 5.4.2 账号管理 DTO/VO

```java
// ================================================================
// 账号 DTO
// ================================================================

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

    /** 角色ID列表 */
    @NotEmpty(message = "至少选择一个角色")
    private List<Long> roleIds;
}

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

@Data
public class AccountResetPasswordRequest {
    @NotNull(message = "账号ID不能为空")
    private Long id;
}

@Data
public class AccountChangePasswordRequest {
    @NotNull(message = "账号ID不能为空")
    private Long id;

    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 100, message = "新密码长度6-100位")
    private String newPassword;
}

@Data
public class AccountVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private Integer status;
    private String statusDesc;
    private String lastLoginTime;
    private String lastLoginIp;
    private List<RoleSimpleVO> roles;
    private LocalDateTime createdAt;
}

@Data
public class RoleSimpleVO {
    private Long id;
    private String roleCode;
    private String roleName;
}
```

---

## 6. RESTful API 接口定义

### 6.1 接口规范（阿里巴巴《Java开发手册》强制）

| 规范 | 说明 |
|------|------|
| **【强制】URL 动宾结构** | `GET /customer/{id}` `POST /customer` `PUT /customer/{id}` `DELETE /customer/{id}` |
| **【强制】禁止动词** | 不用 `getCustomer` `deleteCustomer`，用 REST 风格 |
| **【强制】分页用 `page` 参数** | `GET /customer/page?pageNum=1&pageSize=10` |
| **【强制】路径变量 id 在最后** | `/customer/{id}` 而不是 `/{id}/customer` |
| **【强制】POST/PUT 请求体 JSON** | `@RequestBody` 注解，Content-Type: application/json |
| **【强制】响应 HTTP Status** | 200=成功，400=参数错误，401=未认证，403=禁止，404=不存在，500=系统异常 |
| **【强制】禁止 HTTP Status 200 返回错误** | 业务错误必须返回 `{code: 4xxxx, message: "..."}` 而非 404/500 |
| **【强制】文件下载用 application/octet-stream** | `@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)` |
| **【强制】批量操作加校验** | `List<@Valid T>` 每个元素都校验 |

### 6.2 auth-service 接口

```
认证模块 — dafuweng-auth (端口 8081)
=====================================================================

POST   /auth/login                    账号密码登录
POST   /auth/logout                   登出
POST   /auth/refresh                  刷新Token
GET    /auth/userinfo                 获取当前用户信息+角色+部门

请求示例：
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "captchaCode": "a1b2",
  "captchaKey": "uuid-xxx"
}

响应示例：
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userInfo": {
      "userId": 1,
      "username": "admin",
      "realName": "系统管理员",
      "roles": ["SUPER_ADMIN"],
      "permissions": ["*"]
    }
  },
  "timestamp": 1743427200000
}
```

### 6.3 sales-service 接口

```
销售模块 — dafuweng-sales (端口 8082)
=====================================================================

# ---------- 客户管理 ----------

POST   /customer/check                 客户查重（姓名+手机）
POST   /customer                       新增客户
GET    /customer/{id}                  客户详情
PUT    /customer/{id}                  更新客户信息
DELETE /customer/{id}                  删除客户（逻辑删除）
GET    /customer/page                  分页查询客户
PUT    /customer/{id}/transfer         客户迁移（部门经理操作）
PUT    /customer/{id}/annotate         客户批注
GET    /customer/public-sea             公海客户列表
PUT    /customer/{id}/claim             领取公海客户

请求示例：
POST /customer
Content-Type: application/json

{
  "name": "张三",
  "phone": "13800138000",
  "idCard": "110101199001011234",
  "customerType": 1,
  "intentionLevel": 2,
  "loanIntentionAmount": 500000.00,
  "loanIntentionProduct": "企业经营贷",
  "source": "phone_call"
}

# ---------- 洽谈记录 ----------

POST   /contact-record                 新增洽谈记录
GET    /contact-record/customer/{customerId}  查看客户洽谈历史

# ---------- 合同管理 ----------

POST   /contract                       创建合同（草稿）
GET    /contract/{id}                  合同详情
PUT    /contract/{id}                  更新合同
DELETE /contract/{id}                  删除合同（草稿状态）
PUT    /contract/{id}/sign             签署合同
POST   /contract/{id}/attachment        上传合同附件
PUT    /contract/{id}/pay-first         确认首期服务费已付
POST   /contract/{id}/send-finance      发送至金融部审核
GET    /contract/page                   分页查询合同

# ---------- 工作日志 ----------

POST   /work-log                       提交工作日志
GET    /work-log/{id}                  工作日志详情
PUT    /work-log/{id}                  更新工作日志
GET    /work-log/stats                  统计报表

# ---------- 业绩 ----------

GET    /performance/rep/{repId}         销售代表业绩
GET    /performance/dept/{deptId}       部门业绩
GET    /performance/zone/{zoneId}       战区业绩
GET    /performance/ranking              业绩排名
GET    /performance/analysis             业绩分析
GET    /performance/page                 分页查询业绩记录
```

### 6.4 finance-service 接口

```
金融模块 — dafuweng-finance (端口 8083)
=====================================================================

# ---------- 金融产品 ----------

POST   /product                        新增产品
PUT    /product/{id}                   更新产品
DELETE /product/{id}                   删除产品（下架）
GET    /product/{id}                   产品详情
GET    /product/list                   产品列表（不分页）
GET    /product/page                    分页查询产品

# ---------- 贷款审核 ----------

GET    /loan-audit/received             接收的合同列表（金融专员）
POST   /loan-audit/{id}/receive          接收合同
POST   /loan-audit/{id}/review           初审（推荐产品）
POST   /loan-audit/{id}/submit-bank      提交银行审核
POST   /loan-audit/{id}/bank-result      银行结果反馈
POST   /loan-audit/{id}/reject           拒绝申请
GET    /loan-audit/{id}                 审核详情
GET    /loan-audit/{id}/history          审核历史

# ---------- 服务费 ----------

POST   /service-fee/record              记录服务费
GET    /service-fee/{id}               服务费记录详情
GET    /service-fee/page                分页查询服务费记录
GET    /service-fee/contract/{contractId}  某合同的服务费记录

# ---------- 提成 ----------

GET    /commission/page                  分页查询提成记录
GET    /commission/rep/{repId}           销售提成明细
POST   /commission/confirm/{id}          确认提成
POST   /commission/grant/{id}            发放提成
```

### 6.5 system-service 接口

```
系统模块 — dafuweng-system (端口 8084)
=====================================================================

# ---------- 部门管理 ----------

POST   /department                      创建部门
PUT    /department/{id}                更新部门
DELETE /department/{id}                删除部门
GET    /department/{id}                部门详情
GET    /department/tree                 部门树
GET    /department/list                 部门列表（不分页）

# ---------- 战区管理 ----------

POST   /zone                           创建战区
PUT    /zone/{id}                      更新战区
DELETE /zone/{id}                      删除战区
GET    /zone                           战区列表

# ---------- 账号管理 ----------

POST   /account                        创建账号
PUT    /account/{id}                   更新账号
DELETE /account/{id}                   删除账号
GET    /account/{id}                   账号详情
PUT    /account/{id}/reset-password    重置密码
PUT    /account/{id}/change-password   改密码（本人）
GET    /account/page                    分页查询账号

# ---------- 操作日志 ----------

GET    /operation-log                  操作日志查询
GET    /operation-log/{id}             日志详情

# ---------- 系统参数 ----------

POST   /system-param                   创建参数
PUT    /system-param/{key}             更新参数值
DELETE /system-param/{key}             删除参数
GET    /system-param/{key}             获取参数值
GET    /system-param/page              分页查询参数
```

---

## 7. MapStruct Converter 实现示例

### 7.1 客户 Converter

位置：`dafuweng-sales/src/main/java/.../sales/converter/CustomerConverter.java`

```java
package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerConverter extends EntityConverter<Customer, CustomerVO> {

    // ================================================================
    // Entity → VO（客户详情）
    // ================================================================

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerTypeDesc", source = "customerType",
             qualifiedByName = "customerTypeDesc")
    @Mapping(target = "intentionLevelDesc", source = "intentionLevel",
             qualifiedByName = "intentionLevelDesc")
    @Mapping(target = "statusDesc", source = "status",
             qualifiedByName = "statusDesc")
    @Mapping(target = "annotations", source = "annotation",
             qualifiedByName = "parseAnnotation")
    CustomerVO toVO(Customer entity);

    // ================================================================
    // DTO → Entity（创建客户）
    // ================================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "deptId", ignore = true)
    @Mapping(target = "zoneId", ignore = true)
    @Mapping(target = "status", constant = "1")  // 默认潜在客户
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CustomerCreateRequest request);

    // ================================================================
    // DTO → Entity（更新客户）
    // ================================================================

    @Mapping(target = "id", source = "id")
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "deptId", ignore = true)
    @Mapping(target = "zoneId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CustomerUpdateRequest request);

    // ================================================================
    // 名称映射
    // ================================================================

    @Named("customerTypeDesc")
    default String customerTypeDesc(Integer customerType) {
        if (customerType == null) return null;
        return customerType == 1 ? "个人" : "企业";
    }

    @Named("intentionLevelDesc")
    default String intentionLevelDesc(Integer level) {
        if (level == null) return null;
        return switch (level) {
            case 1 -> "A级(高)";
            case 2 -> "B级(中)";
            case 3 -> "C级(低)";
            case 4 -> "D级(无)";
            default -> "未知";
        };
    }

    @Named("statusDesc")
    default String statusDesc(Integer status) {
        if (status == null) return null;
        return switch (status) {
            case 1 -> "潜在";
            case 2 -> "洽谈中";
            case 3 -> "已签约";
            case 4 -> "已放款";
            case 5 -> "公海";
            default -> "未知";
        };
    }

    @Named("parseAnnotation")
    default List<AnnotationVO> parseAnnotation(String annotation) {
        if (StrUtil.isBlank(annotation)) return List.of();
        try {
            return JSONUtil.toList(annotation, AnnotationVO.class);
        } catch (Exception e) {
            return List.of();
        }
    }
}
```

---

## 8. 全局异常处理器

位置：`dafuweng-common/common-core/.../exception/GlobalExceptionHandler.java`

```java
package com.dafuweng.common.core.exception;

import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.result.R;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局统一异常处理器
 *
 * 【强制】所有 Controller 抛出的异常必须在本类统一处理
 * 【强制】禁止在 Controller 层 try-catch 吞掉异常
 * 【强制】异常消息必须国际化/脱敏，禁止返回原始异常堆栈给前端
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================================================
    // 业务异常
    // ================================================================

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    // ================================================================
    // 参数校验异常（@Valid 校验失败）
    // ================================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return R.error(ErrorCode.PARAM_VALID_ERROR.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining("; "));
        log.warn("约束校验失败: {}", message);
        return R.error(ErrorCode.PARAM_VALID_ERROR.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", message);
        return R.error(ErrorCode.PARAM_VALID_ERROR.getCode(), message);
    }

    // ================================================================
    // 类型转换异常
    // ================================================================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = String.format("参数 '%s' 类型错误，期望值: %s",
            e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        log.warn("类型转换失败: {}", message);
        return R.error(ErrorCode.PARAM_VALID_ERROR.getCode(), message);
    }

    // ================================================================
    // 请求方法不支持
    // ================================================================

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public R<Void> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMessage());
        return R.error(900004, "请求方法不支持: " + e.getMethod());
    }

    // ================================================================
    // 404 异常
    // ================================================================

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("接口不存在: {} {}", e.getHttpMethod(), e.getRequestURL());
        return R.error(900003, "接口不存在");
    }

    // ================================================================
    // 系统异常（兜底）
    // ================================================================

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.error(ErrorCode.SYSTEM_ERROR);
    }
}
```

---

## 9. NOT in Scope

本文档不包含：

- 文件上传下载接口（由专项文档定义）
- 短信/邮件发送接口（由专项文档定义）
- 第三方支付回调接口（由专项文档定义）
- WebSocket 实时推送接口（由专项文档定义）
- 移动端 H5 专用接口（差异化点由专项文档定义）

---

## 10. 实施计划

| 阶段 | 内容 | 产出物 |
|------|------|--------|
| Phase 1 | 定义 R/PageResult/ErrorCode | common-core 代码 |
| Phase 2 | 定义各服务 DTO/VO | domain/dto + domain/vo 包 |
| Phase 3 | 定义 Converter 接口 | converter 包 + MapStruct 配置 |
| Phase 4 | 实现 Controller 层 | RESTful 接口实现 |
| Phase 5 | 全局异常处理器 | ExceptionHandler 实现 |
| Phase 6 | 单元测试 | Converter 测试 + Controller 测试 |
