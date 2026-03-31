# 技术规格说明书 — 大富翁金融服务公司贷款管理系统
**版本:** v1.0
**日期:** 2026-03-31
**技术栈:** SpringBoot3 + MyBatisPlus + MySQL + Redis + Redisson + Nacos + OpenFeign + Spring Gateway + RabbitMQ + Nginx

---

## 1. 技术选型详细说明

### 1.1 核心框架

| 组件 | 版本 | 选型理由 |
|------|------|---------|
| Spring Boot | 3.2.x | Java 17+，最新LTS，虚拟线程支持 |
| Spring Cloud | 2023.0.x | 与SpringBoot3兼容，组件稳定 |
| MyBatisPlus | 3.5.x | CRUD零SQL，代码生成，逻辑删除，自动填充 |
| MySQL | 8.0 | 8.0窗口函数/CTE，性能提升 |
| Redis | 7.x | Redis7新特性（ACL/KV分离） |
| Redisson | 3.27.x | 完整分布式锁/信号量实现，RMapCache |
| Nacos | 2.2.x | 注册中心+配置中心二合一，国产成熟 |
| RabbitMQ | 3.12.x | 死信队列/延迟队列原生支持 |
| Nginx | 1.25.x | 静态资源/SSL/负载均衡 |

### 1.2 依赖版本矩阵

```xml
<!-- 统一版本管理 (parent pom) -->
<spring-boot.version>3.2.5</spring-boot.version>
<spring-cloud.version>2023.0.1</spring-cloud.version>
<spring-cloud-alibaba.version>2023.0.1.2</spring-cloud-alibaba.version>
<mybatis-plus.version>3.5.6</mybatis-plus.version>
<redisson.version>3.27.2</redisson.version>
<mysql.version>8.0.33</mysql.version>
<fastjson2.version>2.0.47</fastjson2.version>
```

---

## 2. 项目模块详细结构

```
dafuweng/
├── pom.xml                          # 父POM（版本管理）
│
├── dafuweng-gateway/                # 网关服务 (端口: 8080)
│   ├── src/main/java/.../gateway/
│   │   ├── GatewayApplication.java
│   │   ├── config/
│   │   │   ├── GatewayConfig.java    # 路由规则
│   │   │   └── AuthGlobalFilter.java # JWT鉴权过滤器
│   │   └── constant/
│   │      WhiteList.java            # 公共路径白名单
│   └── src/main/resources/
│       └── application.yml
│
├── dafuweng-auth/                   # 认证服务 (端口: 8081)
│   ├── src/main/java/.../auth/
│   │   ├── AuthApplication.java
│   │   ├── controller/
│   │   │   └── AuthController.java   # login/refresh/logout
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── impl/AuthServiceImpl.java
│   │   ├── service/AuthService.java
│   │   ├── mapper/
│   │   │   ├── SysUserMapper.java
│   │   │   ├── SysRoleMapper.java
│   │   │   └── SysUserRoleMapper.java
│   │   ├── entity/
│   │   │   ├── SysUser.java
│   │   │   ├── SysRole.java
│   │   │   ├── SysUserRole.java
│   │   │   └── SysPermission.java
│   │   ├── domain/dto/
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   └── RefreshRequest.java
│   │   ├── security/
│   │   │   ├── JwtUtil.java         # JWT工具类
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── PasswordEncoder.java
│   │   └── constant/
│   │       AuthConstant.java
│   └── src/main/resources/
│       ├── mapper/auth/*.xml
│       └── application.yml
│
├── dafuweng-sales/                  # 销售服务 (端口: 8082)
│   ├── src/main/java/.../sales/
│   │   ├── SalesApplication.java
│   │   ├── controller/
│   │   │   ├── CustomerController.java
│   │   │   ├── ContactRecordController.java
│   │   │   ├── ContractController.java
│   │   │   ├── WorkLogController.java
│   │   │   └── PerformanceController.java
│   │   ├── service/
│   │   │   ├── CustomerService.java
│   │   │   ├── ContractService.java
│   │   │   └── impl/
│   │   ├── mapper/
│   │   │   ├── CustomerMapper.java
│   │   │   ├── ContractMapper.java
│   │   │   └── WorkLogMapper.java
│   │   ├── entity/
│   │   │   ├── Customer.java
│   │   │   ├── ContactRecord.java
│   │   │   ├── Contract.java
│   │   │   ├── WorkLog.java
│   │   │   ├── ContractAttachment.java
│   │   │   └── PerformanceRecord.java
│   │   ├── domain/dto/
│   │   │   ├── CustomerCreateRequest.java
│   │   │   ├── CustomerUpdateRequest.java
│   │   │   ├── ContractCreateRequest.java
│   │   │   ├── ContractSignRequest.java
│   │   │   ├── CustomerTransferRequest.java
│   │   │   └── WorkLogSubmitRequest.java
│   │   ├── enums/
│   │   │   ├── CustomerType.java     # 个人/企业
│   │   │   ├── CustomerStatus.java   # 潜在/洽谈中/已签约/已放款/公海
│   │   │   ├── IntentionLevel.java   # A/B/C/D
│   │   │   ├── ContractStatus.java   # 草稿/已签署/已支付首期/审核中/已通过/已拒绝/已放款/已完成
│   │   │   └── ContactType.java      # 电话/面谈/转介绍
│   │   └── config/
│   │       └── RabbitMQSalesConfig.java
│   └── src/main/resources/
│       ├── mapper/sales/*.xml
│       └── application.yml
│
├── dafuweng-finance/                # 金融服务 (端口: 8083)
│   ├── src/main/java/.../finance/
│   │   ├── FinanceApplication.java
│   │   ├── controller/
│   │   │   ├── LoanAuditController.java
│   │   │   ├── FinanceProductController.java
│   │   │   ├── PerformanceController.java
│   │   │   └── ServiceFeeController.java
│   │   ├── service/
│   │   ├── mapper/
│   │   ├── entity/
│   │   │   ├── FinanceProduct.java
│   │   │   ├── LoanAudit.java
│   │   │   ├── LoanAuditRecord.java
│   │   │   ├── ServiceFeeRecord.java
│   │   │   └── CommissionRecord.java
│   │   ├── domain/dto/
│   │   └── enums/
│   │       ├── AuditStatus.java      # 待审核/审核中/已通过/已拒绝
│   │       └── BankAuditStatus.java  # 银行审核状态
│   └── src/main/resources/
│
├── dafuweng-system/                 # 系统管理 (端口: 8084)
│   ├── src/main/java/.../system/
│   │   ├── SystemApplication.java
│   │   ├── controller/
│   │   │   ├── DepartmentController.java
│   │   │   ├── AccountController.java
│   │   │   ├── OperationLogController.java
│   │   │   └── SystemParamController.java
│   │   ├── service/
│   │   ├── mapper/
│   │   └── entity/
│   │       ├── SysDepartment.java
│   │       ├── SysAccount.java
│   │       ├── SysOperationLog.java
│   │       └── SysParam.java
│   └── src/main/resources/
│
├── dafuweng-notify/                 # 消息通知 (端口: 8085)
│   ├── src/main/java/.../notify/
│   │   ├── NotifyApplication.java
│   │   ├── mq/
│   │   │   ├── MessageProducer.java
│   │   │   ├── contract/
│   │   │   │   └── ContractSignedConsumer.java
│   │   │   ├── loan/
│   │   │   │   ├── LoanApprovedConsumer.java
│   │   │   │   └── LoanRejectedConsumer.java
│   │   │   └── publicsea/
│   │   │       └── CustomerPublicSeaConsumer.java
│   │   ├── job/
│   │   │   ├── CustomerPublicSeaJob.java    # 定时扫描入公海
│   │   │   ├── MonthlyPerformanceJob.java    # 月度业绩报表
│   │   │   └── DailyReconciliationJob.java   # 日对账
│   │   └── config/
│   │       └── RabbitMQConfig.java
│   └── src/main/resources/
│
└── dafuweng-common/                 # 公共模块（不打包为服务）
    ├── pom.xml
    │
    ├── common-core/                  # 核心实体/枚举/异常
    │   ├── src/main/java/.../common/core/
    │   │   ├── entity/
    │   │   │   ├── BaseEntity.java           # 所有实体基类
    │   │   │   │   // id, createdAt, updatedAt, createdBy, updatedBy, deleted
    │   │   │   └── BasePageEntity.java        # 分页实体基类
    │   │   ├── enums/
    │   │   │   └── GlobalErrorCode.java       # 统一错误码
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   ├── BusinessException.java
    │   │   │   └── UnauthorizedException.java
    │   │   ├── result/
    │   │   │   ├── R.java                    # 统一响应结构
    │   │   │   └── PageResult.java
    │   │   └── constant/
    │   │       └── DataConstant.java
    │
    ├── common-mybatis/               # MyBatisPlus增强
    │   ├── src/main/java/.../common/mybatis/
    │   │   ├── config/
    │   │   │   ├── MybatisPlusConfig.java
    │   │   │   │   // 分页插件 + 逻辑删除 + 自动填充
    │   │   │   └── DataScopeInterceptor.java  # 数据权限拦截器
    │   │   ├── handler/
    │   │   │   ├── FillMetaObjectHandler.java # 自动填充（创建人/时间）
    │   │   │   └── JsonTypeHandler.java       # JSON字段处理
    │   │   └── injection/
    │   │       └── CustomSqlInjector.java    # 自定义SQL注入
    │
    ├── common-redis/                 # Redis增强
    │   ├── src/main/java/.../common/redis/
    │   │   ├── config/
    │   │   │   └── RedisConfig.java
    │   │   └── service/
    │   │       ├── RedisService.java
    │   │       └── impl/RedisServiceImpl.java
    │
    └── common-feign/                 # Feign增强
        ├── src/main/java/.../common/feign/
        │   ├── config/
        │   │   └── FeignConfig.java
        │   ├── interceptor/
        │   │   └── FeignAuthInterceptor.java  # Token透传
        │   └── fallback/
        │       └── AuthServiceFallback.java
        │
        └── src/main/resources/
            └── META-INF/spring.factories
```

---

## 3. 数据库设计

### 3.1 库隔离策略

| 服务 | 数据库名 | 字符集 |
|------|---------|--------|
| auth-service | dafuweng_auth | utf8mb4 |
| sales-service | dafuweng_sales | utf8mb4 |
| finance-service | dafuweng_finance | utf8mb4 |
| system-service | dafuweng_system | utf8mb4 |

### 3.2 核心表结构（MySQL DDL）

```sql
-- =============================================
-- dafuweng_sales 库
-- =============================================

-- 客户表
CREATE TABLE `customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '客户姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `id_card` VARCHAR(20) COMMENT '身份证号',
  `company_name` VARCHAR(200) COMMENT '企业名称（企业客户）',
  `customer_type` TINYINT NOT NULL DEFAULT 1 COMMENT '客户类型：1-个人 2-企业',
  `sales_rep_id` BIGINT NOT NULL COMMENT '负责销售ID',
  `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
  `zone_id` BIGINT COMMENT '所属战区ID',
  `intention_level` TINYINT DEFAULT 3 COMMENT '意向等级：1-A 2-B 3-C 4-D',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-潜在 2-洽谈中 3-已签约 4-已放款 5-公海',
  `last_contact_date` DATETIME COMMENT '最后联系日期',
  `public_sea_time` DATETIME COMMENT '进入公海时间',
  `annotation` TEXT COMMENT '批注（JSON数组）',
  `created_by` BIGINT NOT NULL COMMENT '创建人',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` BIGINT COMMENT '修改人',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删 1-已删',
  `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_phone` (`name`, `phone`, `deleted`),
  KEY `idx_sales_rep_id` (`sales_rep_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`),
  KEY `idx_public_sea_time` (`public_sea_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 洽谈记录表
CREATE TABLE `contact_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL COMMENT '客户ID',
  `sales_rep_id` BIGINT NOT NULL COMMENT '销售ID',
  `contact_type` TINYINT NOT NULL COMMENT '联系类型：1-电话 2-面谈 3-转介绍',
  `content` TEXT NOT NULL COMMENT '洽谈内容',
  `intention_after` TINYINT COMMENT '洽谈后意向等级',
  `contact_date` DATETIME NOT NULL COMMENT '联系时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_sales_rep_id` (`sales_rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='洽谈记录表';

-- 合同表
CREATE TABLE `contract` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract_no` VARCHAR(50) NOT NULL COMMENT '合同编号',
  `customer_id` BIGINT NOT NULL COMMENT '客户ID',
  `sales_rep_id` BIGINT NOT NULL COMMENT '销售ID',
  `dept_id` BIGINT NOT NULL COMMENT '部门ID',
  `product_id` BIGINT COMMENT '金融产品ID（审核后填充）',
  `contract_amount` DECIMAL(15,2) NOT NULL COMMENT '合同金额',
  `actual_loan_amount` DECIMAL(15,2) COMMENT '实际放款金额（银行放款后）',
  `service_fee_rate` DECIMAL(5,4) NOT NULL COMMENT '服务费率',
  `service_fee_1` DECIMAL(15,2) NOT NULL COMMENT '首期服务费',
  `service_fee_2` DECIMAL(15,2) NOT NULL COMMENT '二期服务费',
  `service_fee_1_paid` TINYINT NOT NULL DEFAULT 0 COMMENT '首期是否已付：0-否 1-是',
  `service_fee_2_paid` TINYINT NOT NULL DEFAULT 0 COMMENT '二期是否已付：0-否 1-是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-草稿 2-已签署 3-已支付首期 4-审核中 5-已通过 6-已拒绝 7-已放款 8-已完成',
  `sign_date` DATE COMMENT '签署日期',
  `finance_send_time` DATETIME COMMENT '发送至金融部时间',
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` BIGINT,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_no` (`contract_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_sales_rep_id` (`sales_rep_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- 合同附件表
CREATE TABLE `contract_attachment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract_id` BIGINT NOT NULL,
  `attachment_type` VARCHAR(50) NOT NULL COMMENT '附件类型：business_license/id_card/other',
  `file_url` VARCHAR(500) NOT NULL COMMENT '文件URL',
  `file_name` VARCHAR(200) NOT NULL COMMENT '原始文件名',
  `file_size` BIGINT COMMENT '文件大小(字节)',
  `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同附件表';

-- 工作日志表
CREATE TABLE `work_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sales_rep_id` BIGINT NOT NULL,
  `log_date` DATE NOT NULL COMMENT '日志日期',
  `calls_made` INT NOT NULL DEFAULT 0 COMMENT '打电话数',
  `effective_calls` INT NOT NULL DEFAULT 0 COMMENT '有效电话数',
  `intention_clients` INT NOT NULL DEFAULT 0 COMMENT '意向客户数',
  `face_to_face_clients` INT NOT NULL DEFAULT 0 COMMENT '面谈客户数',
  `content` TEXT COMMENT '备注内容',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rep_date` (`sales_rep_id`, `log_date`),
  KEY `idx_sales_rep_id` (`sales_rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作日志表';

-- 业绩记录表
CREATE TABLE `performance_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sales_rep_id` BIGINT NOT NULL,
  `dept_id` BIGINT NOT NULL,
  `zone_id` BIGINT,
  `contract_id` BIGINT NOT NULL,
  `contract_amount` DECIMAL(15,2) NOT NULL COMMENT '合同金额',
  `commission_rate` DECIMAL(5,4) NOT NULL COMMENT '提成比例',
  `commission_amount` DECIMAL(15,2) NOT NULL COMMENT '提成金额',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-计算中 2-已确认 3-已发放',
  `calculated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmed_at` DATETIME,
  `granted_at` DATETIME,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_sales_rep_id` (`sales_rep_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业绩记录表';

-- =============================================
-- dafuweng_finance 库
-- =============================================

-- 金融产品表
CREATE TABLE `finance_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '产品名称',
  `bank_id` BIGINT NOT NULL COMMENT '合作银行ID',
  `bank_name` VARCHAR(100) COMMENT '银行名称',
  `min_amount` DECIMAL(15,2) NOT NULL COMMENT '最小金额',
  `max_amount` DECIMAL(15,2) NOT NULL COMMENT '最大金额',
  `interest_rate` DECIMAL(6,4) NOT NULL COMMENT '利率',
  `min_term` INT NOT NULL COMMENT '最小期限（月）',
  `max_term` INT NOT NULL COMMENT '最大期限（月）',
  `requirements` TEXT COMMENT '申请条件（JSON）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-可用 0-停用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_bank_id` (`bank_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='金融产品表';

-- 贷款审核表
CREATE TABLE `loan_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract_id` BIGINT NOT NULL COMMENT '合同ID',
  `finance_specialist_id` BIGINT COMMENT '负责金融专员ID',
  `recommended_product_id` BIGINT COMMENT '推荐产品ID',
  `audit_status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-待审核 2-审核中 3-已通过 4-已拒绝',
  `bank_id` BIGINT COMMENT '银行ID',
  `bank_audit_status` VARCHAR(20) COMMENT '银行审核状态：pending/approved/rejected',
  `bank_feedback` TEXT COMMENT '银行反馈内容',
  `reject_reason` VARCHAR(500) COMMENT '拒绝原因',
  `audit_date` DATETIME COMMENT '审核日期',
  `submit_bank_date` DATETIME COMMENT '提交银行日期',
  `loan_granted_date` DATETIME COMMENT '银行放款日期',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_id` (`contract_id`),
  KEY `idx_finance_specialist_id` (`finance_specialist_id`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款审核表';

-- 审核记录表（审计历史）
CREATE TABLE `loan_audit_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `loan_audit_id` BIGINT NOT NULL,
  `operator_id` BIGINT NOT NULL COMMENT '操作人ID',
  `operator_name` VARCHAR(50) COMMENT '操作人姓名',
  `action` VARCHAR(50) NOT NULL COMMENT '操作类型：review/submit_bank/bank_result/approve/reject',
  `content` TEXT COMMENT '操作内容',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_loan_audit_id` (`loan_audit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款审核记录表';

-- 服务费记录表
CREATE TABLE `service_fee_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract_id` BIGINT NOT NULL COMMENT '合同ID',
  `fee_type` TINYINT NOT NULL COMMENT '费用类型：1-首期 2-二期',
  `amount` DECIMAL(15,2) NOT NULL COMMENT '金额',
  `payment_method` VARCHAR(20) COMMENT '支付方式：bank_transfer/wechat/alipay/cash',
  `payment_date` DATE COMMENT '支付日期',
  `accountant_id` BIGINT NOT NULL COMMENT '会计ID',
  `remark` VARCHAR(500) COMMENT '备注',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务费记录表';

-- =============================================
-- dafuweng_auth 库
-- =============================================

-- 用户表
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(200) NOT NULL COMMENT '密码（BCrypt）',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `dept_id` BIGINT COMMENT '部门ID',
  `zone_id` BIGINT COMMENT '战区ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `login_error_count` INT NOT NULL DEFAULT 0 COMMENT '连续登录错误次数',
  `lock_time` DATETIME COMMENT '锁定截止时间',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 角色表
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '数据范围：1-本人 2-本部门 3-本战区 4-全部',
  `status` TINYINT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- =============================================
-- dafuweng_system 库
-- =============================================

-- 部门表
CREATE TABLE `sys_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
  `parent_id` BIGINT COMMENT '父部门ID',
  `zone_id` BIGINT NOT NULL COMMENT '战区ID',
  `manager_id` BIGINT COMMENT '部门经理ID',
  `sort_order` INT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_zone_id` (`zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 战区表
CREATE TABLE `sys_zone` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '战区名称',
  `director_id` BIGINT COMMENT '销售总监ID',
  `sort_order` INT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='战区表';

-- 操作日志表
CREATE TABLE `sys_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `module` VARCHAR(50) NOT NULL COMMENT '模块',
  `action` VARCHAR(100) NOT NULL COMMENT '操作类型',
  `request_method` VARCHAR(10) COMMENT '请求方法',
  `request_url` VARCHAR(500) COMMENT '请求URL',
  `request_params` TEXT COMMENT '请求参数',
  `response_code` VARCHAR(20) COMMENT '响应状态码',
  `error_msg` TEXT COMMENT '错误信息',
  `ip` VARCHAR(50) COMMENT 'IP地址',
  `user_agent` VARCHAR(500) COMMENT 'UserAgent',
  `cost_time` BIGINT COMMENT '耗时(毫秒)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统参数表
CREATE TABLE `sys_param` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `param_key` VARCHAR(100) NOT NULL COMMENT '参数键',
  `param_value` TEXT NOT NULL COMMENT '参数值',
  `param_type` VARCHAR(50) COMMENT '类型：string/int/json',
  `remark` VARCHAR(200) COMMENT '备注',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';
```

---

## 4. API 接口设计

### 4.1 统一响应格式

```java
// 成功响应
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": 1711872000000
}

// 分页响应
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [...],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  },
  "timestamp": 1711872000000
}

// 错误响应
{
  "code": 40001,
  "message": "业务错误描述",
  "data": null,
  "timestamp": 1711872000000
}
```

### 4.2 错误码规范

| 区间 | 模块 |
|------|------|
| 10001-10099 | 认证模块 |
| 20001-20099 | 销售模块 |
| 30001-30099 | 金融模块 |
| 40001-40099 | 系统管理 |
| 50001-50099 | 公共模块 |

### 4.3 核心接口列表

#### 认证接口（auth-service, 端口8081）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/login | 账号密码登录 |
| POST | /auth/logout | 登出 |
| POST | /auth/refresh | 刷新Token |
| GET | /auth/userinfo | 获取当前用户信息 |

#### 客户接口（sales-service, 端口8082）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /customer/check | 查重（姓名+手机） |
| POST | /customer | 新增客户 |
| GET | /customer/{id} | 客户详情 |
| PUT | /customer/{id} | 更新客户 |
| DELETE | /customer/{id} | 删除客户 |
| GET | /customer/page | 分页查询（支持多维度筛选） |
| PUT | /customer/{id}/transfer | 客户迁移 |
| PUT | /customer/{id}/annotate | 客户批注 |
| GET | /customer/public-sea | 公海客户列表 |
| PUT | /customer/{id}/claim | 领取公海客户 |

#### 洽谈接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /contact-record | 新增洽谈记录 |
| GET | /contact-record/customer/{customerId} | 客户洽谈历史 |

#### 合同接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /contract | 创建合同（草稿） |
| PUT | /contract/{id}/sign | 签署合同 |
| POST | /contract/{id}/attachment | 上传合同附件 |
| PUT | /contract/{id}/pay-first | 确认首期服务费 |
| POST | /contract/{id}/send-finance | 发送至金融部 |
| GET | /contract/{id} | 合同详情 |

#### 金融接口（finance-service, 端口8083）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /loan-audit/received | 接收的合同列表 |
| POST | /loan-audit/{id}/review | 初审 |
| POST | /loan-audit/{id}/submit-bank | 提交银行 |
| POST | /loan-audit/{id}/bank-result | 银行结果反馈 |
| GET | /loan-audit/{id}/history | 审核历史 |

---

## 5. Nacos 配置规范

### 5.1 配置文件命名

每个服务在 Nacos 有两份配置：
- `{service-name}.yml` — 共享配置
- `{service-name}-{profile}.yml` — 环境特定配置（dev/test/pre/prod）

### 5.2 共享配置项（common.yml）

```yaml
# Nacos 共享配置
spring:
  application:
    name: dafuweng-sales
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:dafuweng_sales}?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
    username: ${DB_USER:root}
    password: ${DB_PWD:root123}
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PWD:}
    database: ${REDIS_DB:0}

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.dafuweng.**.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# Redisson
redisson:
  address: redis://${REDIS_HOST:localhost}:${REDIS_PORT:6379}
  password: ${REDIS_PWD:}
  database: ${REDIS_DB:0}

# Nacos 服务注册
spring.cloud.nacos.discovery:
  namespace: ${NACOS_NAMESPACE:public}
  group: ${NACOS_GROUP:DEFAULT_GROUP}
  enabled: true

# JWT配置
jwt:
  secret: ${JWT_SECRET:}  # 生产必须从环境变量注入，不能写死在配置
  expiration: 86400000     # 24小时
  refresh-expiration: 604800000  # 7天
```

---

## 6. RabbitMQ 队列设计

### 6.1 交换机与队列

```java
// 交换机
Exchange contractExchange = new DirectExchange("contract.exchange");      // 合同事件
Exchange loanExchange = new DirectExchange("loan.exchange");              // 贷款事件
Exchange publicSeaExchange = new DirectExchange("publicsea.exchange");    // 公海事件
Exchange delayExchange = new DirectExchange("delay.exchange", true);      // 延迟交换机（死信）

// 队列
Queue contractSignedQueue = new Queue("contract.signed.queue");           // 合同签署
Queue loanApprovedQueue = new Queue("loan.approved.queue");              // 贷款批准
Queue customerPublicSeaQueue = new Queue("customer.publicsea.queue");    // 客户入公海
Queue customerPublicSeaDelayQueue = new Queue("customer.publicsea.delay.queue", true); // 延迟队列（TTL后进入DLX）

// 绑定
// 合同签署 → 通知金融部
Binding.bind(contractSignedQueue).to(contractExchange).with("contract.signed");
// 客户入公海延迟队列 → DLX → 正式队列
queueBinding.bind(delayExchange).to("X").with("customer.publicsea.routing.key")
    .withArgument("x-dead-letter-exchange", "publicsea.exchange")
    .withArgument("x-dead-letter-routing-key", "customer.publicsea.routing.key")
    .withArgument("x-message-ttl", 86400000 * N); // N天
```

### 6.2 消息格式

```json
// 合同签署消息
{
  "eventType": "CONTRACT_SIGNED",
  "contractId": 12345,
  "customerId": 67890,
  "salesRepId": 111,
  "timestamp": "2026-03-31T10:00:00Z"
}

// 客户入公海延迟消息（放入延迟队列）
{
  "eventType": "CUSTOMER_PUBLIC_SEA_CHECK",
  "customerId": 67890,
  "createdAt": "2026-03-31T10:00:00Z"
}
```

---

## 7. Redis 缓存与锁设计

### 7.1 缓存Key规范

```
# 用户Token黑名单（logout时写入）
blacklist:token:{token}  →  1  TTL=JWT过期时间

# 用户会话信息
session:user:{userId}  →  {userId, username, roles[], permissions[]}
  TTL=24h

# 客户查重缓存（防止并发录入）
customer:duplicate:check:{name}:{phone}  →  {customerId}  TTL=5分钟

# 公海客户领取锁
lock:customer:claim:{customerId}  →  {userId}  TTL=30秒

# 合同状态变更锁（防止并发修改）
lock:contract:status:{contractId}  →  {userId}  TTL=10秒

# 业绩计算锁（防止重复计算）
lock:performance:calculate:{contractId}  →  {userId}  TTL=60秒

# 验证码
captcha:{captchaKey}  →  {captchaCode}  TTL=5分钟
```

### 7.2 Redisson分布式锁

```java
// 获取锁（等待5秒，锁定30秒自动释放）
RLock lock = redissonClient.getLock("lock:customer:claim:" + customerId);
boolean locked = lock.tryLock(5, 30, TimeUnit.SECONDS);

// 释放锁
if (lock.isHeldByCurrentThread()) {
    lock.unlock();
}

// 公平锁（按请求顺序获取）
RLock fairLock = redissonClient.getFairLock("fair:lock:performance:" + contractId);
```

---

## 8. Nginx 配置

```nginx
# /etc/nginx/nginx.conf

worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
    use epoll;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for" '
                    'rt=$request_time';

    access_log /var/log/nginx/access.log main;

    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    gzip on;
    gzip_types text/plain application/json application/xml text/css application/javascript;

    # 上游服务（Gateway）
    upstream dafuweng_gateway {
        least_conn;
        server 127.0.0.1:8080 weight=1;
        # 多实例部署时：
        # server 127.0.0.1:8080 weight=1;
        # server 127.0.0.1:8086 weight=1;
        keepalive 32;
    }

    server {
        listen 80;
        server_name localhost;

        # 静态资源
        location /static/ {
            alias /usr/share/nginx/html/static/;
            expires 30d;
            add_header Cache-Control "public, immutable";
        }

        # API 转发到 Gateway
        location /api/ {
            proxy_pass http://dafuweng_gateway;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;

            # 超时配置
            proxy_connect_timeout 10s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;

            # 开启 gzip
            proxy_http_version 1.1;
            proxy_set_header Connection "";
        }

        # 文件上传大小限制
        client_max_body_size 50m;
    }
}
```

---

## 9. Spring Gateway 路由配置

```yaml
# dafuweng-gateway/src/main/resources/application.yml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: false  # 关闭服务自动发现，手动配置路由
      routes:
        # 认证服务
        - id: auth-service
          uri: lb://dafuweng-auth
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200

        # 销售服务
        - id: sales-service
          uri: lb://dafuweng-sales
          predicates:
            - Path=/customer/**,/contact-record/**,/contract/**,/work-log/**,/performance/**
          filters:
            - StripPrefix=1

        # 金融服务
        - id: finance-service
          uri: lb://dafuweng-finance
          predicates:
            - Path=/loan-audit/**,/product/**,/service-fee/**
          filters:
            - StripPrefix=1

        # 系统管理
        - id: system-service
          uri: lb://dafuweng-system
          predicates:
            - Path=/department/**,/account/**,/operation-log/**,/system-param/**
          filters:
            - StripPrefix=1

        # 消息通知（内部服务，不对外暴露）
        # - id: notify-service
        #   uri: lb://dafuweng-notify
        #   predicates:
        #     - Path=/notify/**
        #   filters:
        #     - StripPrefix=1
```

---

## 10. OpenFeign 服务间调用

### 10.1 Feign Client定义

```java
// 在 dafuweng-common/common-feign 中定义

@FeignClient(name = "dafuweng-auth", fallback = AuthServiceFallback.class)
public interface AuthFeignClient {

    @GetMapping("/auth/userinfo")
    R<SysUserDTO> getUserInfo(@RequestHeader("Authorization") String token);

    @PostMapping("/auth/verify")
    R<Boolean> verifyToken(@RequestHeader("Authorization") String token);
}

// 销售服务调用金融服务的Feign
@FeignClient(name = "dafuweng-finance")
public interface FinanceFeignClient {

    @PostMapping("/loan-audit/receive")
    R receiveContract(@RequestBody ContractReceiveRequest request);

    @GetMapping("/loan-audit/{id}/history")
    R<List<AuditHistoryDTO>> getAuditHistory(@PathVariable("id") Long auditId);
}
```

### 10.2 Feign Token透传

```java
// FeignAuthInterceptor - 自动透传Authorization头
@Component
public class FeignAuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从当前线程上下文获取Token（Gateway写入）
        String token = SecurityContextHolder.getToken();
        if (StrUtil.isNotBlank(token)) {
            template.header("Authorization", token);
        }
    }
}
```

---

## 11. 数据权限实现（MyBatisPlus拦截器）

```java
// DataScopeInterceptor — 根据用户角色自动拼接数据权限SQL
@Component
@Intercepts({
    @Intercept(type = StatementHandler.class, method = "prepare")
})
public class DataScopeInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(InterceptPoint point, MetaObject metaObject, MappedStatement ms,
                            ParameterHandler parameterHandler, BoundSql boundSql) {
        // 1. 获取当前用户信息
        LoginUser loginUser = SecurityContextHolder.getCurrentUser();
        if (loginUser == null) return;

        // 2. 获取SQL中的表别名
        String originalSql = boundSql.getSql();
        String tableAlias = getTableAlias(ms.getId()); // e.g., "c" for customer

        // 3. 根据角色拼接WHERE条件
        String dataScopeSql = buildDataScopeSql(loginUser, tableAlias);

        // 4. 注入到WHERE后
        if (StrUtil.isNotBlank(dataScopeSql)) {
            String newSql = originalSql + " AND " + dataScopeSql;
            boundSql.setSql(newSql);
        }
    }

    private String buildDataScopeSql(LoginUser user, String alias) {
        // 本人：只能看自己的客户
        if (user.hasRole("sales_rep")) {
            return String.format("%s.sales_rep_id = %d", alias, user.getUserId());
        }
        // 部门经理：本部门
        if (user.hasRole("dept_manager")) {
            return String.format("%s.dept_id IN (%s)", alias,
                String.join(",", user.getManagedDeptIds()));
        }
        // 销售总监：本战区
        if (user.hasRole("sales_director")) {
            return String.format("%s.zone_id = %d", alias, user.getZoneId());
        }
        // 总经理/管理员：全部
        return "";  // 不加限制
    }
}
```

---

## 12. 关键类代码

### 12.1 统一响应R.java

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

#### PageResult 分页响应

```java
package com.dafuweng.common.core.result;

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

    public static <T> PageResult<T> of(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        return new PageResult<>(
            page.getRecords(),
            page.getTotal(),
            page.getSize(),
            page.getCurrent()
        );
    }

    public static <T> PageResult<T> empty(long size) {
        return new PageResult<>(List.of(), 0, size, 1);
    }
}
```

### 12.2 合同状态枚举

```java
public enum ContractStatus {
    DRAFT(1, "草稿"),
    SIGNED(2, "已签署"),
    FIRST_FEE_PAID(3, "已支付首期"),
    IN_AUDIT(4, "审核中"),
    APPROVED(5, "已通过"),
    REJECTED(6, "已拒绝"),
    LOAN_GRANTED(7, "已放款"),
    COMPLETED(8, "已完成");

    private final int code;
    private final String desc;

    ContractStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 状态流转校验
    public boolean canTransitionTo(ContractStatus target) {
        return switch (this) {
            case DRAFT -> target == SIGNED;
            case SIGNED -> target == FIRST_FEE_PAID;
            case FIRST_FEE_PAID -> target == IN_AUDIT;
            case IN_AUDIT -> target == APPROVED || target == REJECTED;
            case APPROVED -> target == LOAN_GRANTED;
            case LOAN_GRANTED -> target == COMPLETED;
            default -> false;
        };
    }
}
```

### 12.3 JWT工具类

```java
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(LoginUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUserId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),
                         Jwts.SIG.HS256)
                .compact();
    }

    public LoginUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // 构造LoginUser对象
            return LoginUser.builder()
                    .userId(Long.parseLong(claims.getSubject()))
                    .username(claims.get("username", String.class))
                    .roles((List<String>) claims.get("roles"))
                    .build();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(10001, "Token已过期");
        } catch (JwtException e) {
            throw new BusinessException(10002, "Token无效");
        }
    }
}
```

---

## 13. 部署脚本

### 13.1 初始化SQL脚本

```bash
# scripts/init.sql
# 创建4个数据库
CREATE DATABASE IF NOT EXISTS dafuweng_auth DEFAULT CHARSET utf8mb4;
CREATE DATABASE IF NOT EXISTS dafuweng_sales DEFAULT CHARSET utf8mb4;
CREATE DATABASE IF NOT EXISTS dafuweng_finance DEFAULT CHARSET utf8mb4;
CREATE DATABASE IF NOT EXISTS dafuweng_system DEFAULT CHARSET utf8mb4;

# 初始化公共数据
USE dafuweng_auth;
INSERT INTO sys_role (role_code, role_name, data_scope) VALUES
('admin', '系统管理员', 4),
('general_manager', '总经理', 4),
('sales_director', '销售总监', 3),
('dept_manager', '部门经理', 2),
('sales_rep', '销售代表', 1),
('finance_specialist', '金融专员', 1),
('finance_manager', '金融部经理', 2),
('accountant', '会计', 2);
```

### 13.2 Docker构建脚本

```bash
#!/bin/bash
# scripts/build-docker.sh

set -e

SERVICES=("dafuweng-gateway" "dafuweng-auth" "dafuweng-sales"
          "dafuweng-finance" "dafuweng-system" "dafuweng-notify")

for svc in "${SERVICES[@]}"; do
    echo "Building $svc..."
    cd $svc
    mvn clean package -DskipTests spring-boot:repackage
    docker build -t dafuweng/$svc:latest .
    cd ..
done

echo "All services built successfully"
```

---

## 14. 性能与安全基准

| 指标 | 目标 |
|------|------|
| API平均响应时间 | < 200ms (P99 < 500ms) |
| 并发用户数 | 支持 500 并发 |
| 数据库连接池 | HikariCP, 最大20, 最小5 |
| Redis连接池 | 最大30, 超时3s |
| JWT Token长度 | < 1KB |
| 密码加密 | BCrypt, strength=12 |
| SQL注入防护 | MyBatisPlus参数化查询，零字符串拼接 |
| XSS防护 | 全局Filter，HTML转义 |
| CSRF防护 | JWT Token放在Header而非Cookie |
| 文件上传 | 大小限制50MB，类型白名单校验 |

---

## 15. 项目根 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 ...">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.dafuweng</groupId>
    <artifactId>dafuweng-parent</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>
    <name>dafuweng-parent</name>

    <modules>
        <module>dafuweng-common</module>
        <module>dafuweng-gateway</module>
        <module>dafuweng-auth</module>
        <module>dafuweng-sales</module>
        <module>dafuweng-finance</module>
        <module>dafuweng-system</module>
        <module>dafuweng-notify</module>
    </modules>

    <properties>
        <java.version>17</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
        <spring-cloud.version>2023.0.1</spring-cloud.version>
        <spring-cloud-alibaba.version>2023.0.1.2</spring-cloud-alibaba.version>
        <mybatis-plus.version>3.5.6</mybatis-plus.version>
        <redisson.version>3.27.2</redisson.version>
        <mysql.version>8.0.33</mysql.version>
        <fastjson2.version>2.0.47</fastjson2.version>
        <jjwt.version>0.12.5</jjwt.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- SpringBoot -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- SpringCloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- SpringCloud Alibaba -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- MyBatisPlus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>
            <!-- Redisson -->
            <dependency>
                <groupId>org.redisson</groupId>
                <artifactId>redisson-spring-boot-starter</artifactId>
                <version>${redisson.version}</version>
            </dependency>
            <!-- MySQL -->
            <dependency>
                <groupId>com.mysql</groupId>
                <artifactId>mysql-connector-j</artifactId>
                <version>${mysql.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
