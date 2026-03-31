# 实施蓝图 — 大富翁金融服务公司贷款管理系统
**版本:** v1.0
**日期:** 2026-03-31
**状态:** 草稿

---

## 1. 背景与目标

大富翁金融服务公司需要一套数字化贷款管理系统，覆盖销售获客、合同签署、金融审核、贷款发放、业绩统计全流程。系统同时服务PC端（总经理/销售总监/部门经理）和移动端（销售代表手机操作）。

**核心业务流程：**

```
销售代表获客 → 查询是否已存在客户 → 录入系统
    ↓
多次洽谈（记录洽谈历史） → 明确贷款意向
    ↓
签署贷款合同（含附件：营业执照/身份证） → 支付首期服务费
    ↓
金融部审核 → 确定金融产品 → 联系银行 → 银行审核 → 银行放款
    ↓
银行反馈放款结果 → 客户支付二期服务费 → 销售代表业绩提成计算
```

---

## 2. 技术架构

### 2.1 微服务拆分

| 服务名 | 职责 | 技术栈 |
|--------|------|--------|
| gateway-service | 统一网关，路由/鉴权 | Spring Gateway + Nacos |
| auth-service | 账号/角色/权限管理 | SpringBoot3 + MyBatisPlus |
| sales-service | 销售部所有业务 | SpringBoot3 + MyBatisPlus |
| finance-service | 金融部所有业务 | SpringBoot3 + MyBatisPlus |
| system-service | 系统管理（管理员） | SpringBoot3 + MyBatisPlus |
| notify-service | 消息通知（RabbitMQ） | SpringBoot3 + RabbitMQ |

### 2.2 基础设施

```
                    ┌─────────────────────────────────────┐
                    │           Nginx (反向代理)            │
                    │  静态资源 / 负载均衡 / SSL终止          │
                    └──────────────┬──────────────────────┘
                                   │
                    ┌──────────────▼──────────────────────┐
                    │        Spring Cloud Gateway           │
                    │  (Nacos 路由发现 / 统一鉴权 / 限流)      │
                    └──────────────┬──────────────────────┘
                                   │
              ┌────────────────────┼────────────────────┐
              │                    │                    │
   ┌──────────▼──────┐  ┌──────────▼──────┐  ┌────────▼────────┐
   │  auth-service   │  │  sales-service   │  │ finance-service │
   │  (账号/角色)     │  │  (客户/合同/洽谈) │  │ (审核/放款/产品) │
   └─────────────────┘  └──────────────────┘  └─────────────────┘
              │                    │                    │
   ┌──────────▼──────┐  ┌──────────▼──────┐  ┌────────▼────────┐
   │ system-service  │  │  notify-service  │  │  (RabbitMQ)     │
   │  (系统管理)      │  │  (消息队列)        │  │  死信队列定时   │
   └─────────────────┘  └──────────────────┘  └─────────────────┘
                                   │
                    ┌──────────────▼──────────────────────┐
                    │     Nacos (注册中心 + 配置中心)        │
                    │     Redis + Redisson (缓存/锁)         │
                    │     MySQL (主数据库)                   │
                    └─────────────────────────────────────┘
```

### 2.3 服务间通信

- **同步调用：** OpenFeign（服务间HTTP调用，auth-service鉴权token透传）
- **异步事件：** RabbitMQ（订单状态变更、业绩计算触发、定时任务）
- **死信队列定时：** RabbitMQ DLX + 定时消息，实现延迟任务（如：客户N天未签约自动释放到公海）

### 2.4 数据库设计原则

- 每个微服务独立数据库（按领域隔离）
- MyBatisPlus 作为ORM（代码生成器 + 逻辑删除 + 自动填充）
- 公共字段统一抽象（创建时间/修改时间/创建人/修改人）
- 所有表必须有逻辑删除字段 `deleted = 0`

---

## 3. 领域模型

### 3.1 组织架构

```
公司
├── 销售部 (Sales)
│   ├── 战区A
│   │   ├── 销售部1 (部门经理 + N个销售代表)
│   │   └── 销售部2 (部门经理 + N个销售代表)
│   └── 战区B
│       ├── 销售部3 ...
│       └── ...
├── 金融部 (Finance)
│   ├── 金融专员
│   └── 会计
└── 总经理
```

**角色体系：**
| 角色 | 权限范围 |
|------|---------|
| 系统管理员 | 部门增删、账号管理、日志查询、系统参数 |
| 总经理 | 全局数据查看、业绩管理、客户迁移 |
| 销售总监 | 本战区所有数据、统计 |
| 部门经理 | 本部门数据、客户迁移、批注 |
| 销售代表 | 自己客户的增删改查、洽谈记录、签约 |
| 金融专员 | 审核贷款业务、联系银行、记录结果 |
| 金融部经理 | 审核管理、业绩统计 |
| 会计 | 服务费收取记录 |

### 3.2 核心实体关系

```
Customer (客户)
  ├── id, name, phone, idCard, companyName, type(个人/企业)
  ├── salesRepId, deptId, zoneId
  ├── intentionLevel (A/B/C/D)
  ├── status (潜在/洽谈中/已签约/已放款/公海)
  ├── publicSeaTime (进入公海时间)
  └── createdAt, updatedAt, deleted

ContactRecord (洽谈记录)
  ├── id, customerId, salesRepId
  ├── contactType (电话/面谈/转介绍)
  ├── content, intentionChange
  └── contactDate

Contract (合同)
  ├── id, contractNo, customerId, salesRepId
  ├── productId, contractAmount, actualLoanAmount
  ├── serviceFee1, serviceFee2, serviceFee1Paid, serviceFee2Paid
  ├── status (草稿/已签署/已支付首期/审核中/已放款/已完成)
  ├── attachments[] (营业执照, 身份证等)
  ├── signDate, createdAt
  └── financeReviewId

FinanceProduct (金融产品)
  ├── id, name, bankId, minAmount, maxAmount
  ├── interestRate, term, requirements
  └── status

LoanAudit (贷款审核)
  ├── id, contractId, financeSpecialistId
  ├── auditStatus (待审核/审核中/已通过/已拒绝)
  ├── recommendedProductId
  ├── bankId, bankAuditStatus, bankFeedback
  ├── auditRecords[] (审核历史)
  └── auditDate, loanGrantedDate

PerformanceRecord (业绩记录)
  ├── id, salesRepId, contractId
  ├── performanceAmount, commission
  ├── status (计算中/已确认/已发放)
  └── calculatedAt

WorkLog (工作日志)
  ├── id, salesRepId, logDate
  ├── callsMade, effectiveCalls
  ├── intentionClients, faceToFaceClients
  └── content
```

---

## 4. 模块详细设计

### 4.1 auth-service（认证授权服务）

**职责：** 统一账号、角色、权限、SSO

**API：**
- `POST /auth/login` — 账号密码登录，返回JWT
- `POST /auth/refresh` — 刷新Token
- `GET /auth/userinfo` — 获取当前用户信息+角色+部门

**数据结构：**
- `sys_user` — 用户表（userId, username, password, realName, phone, deptId, zoneId, status）
- `sys_role` — 角色表
- `sys_user_role` — 用户角色关联
- `sys_permission` — 权限表（菜单/按钮级）

**安全设计：**
- 密码BCrypt加密
- JWT RS256签名（私钥在Nacos配置中心）
- Redis存储黑名单Token（logout时）
- Redisson实现登录失败次数限制（5次/15分钟锁定）

### 4.2 sales-service（销售服务）

**职责：** 客户管理、洽谈记录、合同签署、销售业绩

**核心功能：**

**客户管理：**
- `POST /customer` — 销售代表新增客户（先查重：姓名+手机号）
- `GET /customer/{id}` — 客户详情
- `PUT /customer/{id}` — 更新客户信息
- `GET /customer/page` — 分页查询（支持按销售代表/部门/战区筛选）
- `PUT /customer/{id}/transfer` — 客户迁移（部门经理操作）
- `PUT /customer/{id}/annotate` — 客户批注
- `GET /customer/public-sea` — 公海客户列表
- `PUT /customer/{id}/claim` — 领取公海客户
- 定时任务：客户N天未签约自动入公海（RabbitMQ延迟队列）

**洽谈记录：**
- `POST /contact-record` — 添加洽谈记录
- `GET /contact-record/customer/{customerId}` — 查看客户洽谈历史

**合同管理：**
- `POST /contract` — 创建合同（草稿）
- `PUT /contract/{id}/sign` — 签署合同（上传附件）
- `PUT /contract/{id}/pay-first` — 确认首期服务费已付
- `GET /contract/{id}` — 合同详情
- `POST /contract/{id}/send-to-finance` — 发送至金融部审核

**工作日志：**
- `POST /work-log` — 提交工作日志
- `GET /work-log/stats` — 统计报表（电话数/有效电话/意向/面签）

**业绩统计：**
- `GET /performance/rep/{repId}` — 销售代表业绩
- `GET /performance/dept/{deptId}` — 部门业绩
- `GET /performance/zone/{zoneId}` — 战区业绩

### 4.3 finance-service（金融服务）

**职责：** 贷款审核、金融产品管理、银行对接、业绩计算

**核心功能：**

**贷款审核：**
- `GET /loan-audit/received` — 接收到的合同列表
- `POST /loan-audit/{id}/review` — 金融专员初审（选择产品）
- `POST /loan-audit/{id}/submit-bank` — 提交银行审核
- `POST /loan-audit/{id}/bank-result` — 接收银行反馈（放款结果）
- `GET /loan-audit/{id}/history` — 审核历史

**金融产品：**
- `POST /product` — 新增产品
- `PUT /product/{id}` — 更新产品
- `GET /product/list` — 产品列表

**业绩计算：**
- `POST /performance/calculate/{contractId}` — 计算某合同业绩（合同金额×提成比例）
- `GET /performance/ranking` — 业绩排名
- `GET /performance/analysis` — 业绩分析

**服务费：**
- `POST /service-fee/record` — 记录服务费收取（会计操作）
- `GET /service-fee/records` — 服务费记录查询

### 4.4 system-service（系统管理）

**职责：** 部门管理、账号管理、系统参数、日志

**核心功能：**
- `POST /department` — 创建部门（需指定战区）
- `PUT /department/{id}` — 更新部门
- `GET /department/tree` — 部门树
- `POST /account` — 创建账号（系统管理员）
- `PUT /account/{id}/reset-password` — 重置密码
- `GET /operation-log` — 操作日志查询
- `PUT /system-param/{key}` — 更新系统参数

### 4.5 notify-service（消息通知）

**职责：** 异步消息处理、定时任务

**消息类型：**
- `contract.signed` — 合同签署事件 → 通知金融部
- `loan.approved` — 贷款批准事件 → 通知销售代表+客户
- `loan.rejected` — 贷款拒绝事件 → 通知销售代表+客户
- `customer.public-sea` — 客户入公海事件 → 通知原销售代表
- `performance.calculated` — 业绩计算完成 → 通知相关人员

**定时任务（RabbitMQ DLX）：**
- 每天扫描：客户N天未签约 → 入公海
- 每月1号：生成上月业绩报表
- 每天凌晨：服务费到账对账

---

## 5. 接口安全设计

```
请求流程：
Client → Nginx → Gateway → Nacos (路由) → 目标微服务

鉴权流程：
1. Client 登录 auth-service 获取 JWT
2. Client 请求带上 Header: Authorization: Bearer <token>
3. Gateway 验证 JWT（公共路径白名单：/auth/login, /auth/captcha）
4. Feign 调用时透传 Authorization 头
5. 目标服务根据 UserId 查 Redis 获取当前用户角色+权限
```

**数据权限控制（MyBatisPlus拦截器）：**
- 销售代表：只能看自己的客户
- 部门经理：看本部门所有客户
- 销售总监：看本战区所有客户
- 金融专员：只能看已签署合同的客户
- 总经理/管理员：看全部

实现：在 DAO 层注入 `DataScopeInterceptor`，根据用户角色拼接 SQL 的 WHERE 条件

---

## 6. 部署架构

### 6.1 环境划分

| 环境 | 用途 |
|------|------|
| dev | 开发本地 |
| test | 测试环境 |
| pre | 预发布（和生产一致，用于最终验收） |
| prod | 生产环境 |

### 6.2 Docker Compose 本地开发

```yaml
# docker-compose.yml (本地完整环境)
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: dafuweng
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7
    ports:
      - "6379:6379"

  nacos:
    image: nacos/nacos-server:v2.2.3
    environment:
      MODE: standalone
    ports:
      - "8848:8848"
      - "9848:9848"

  rabbitmq:
    image: rabbitmq:3.12-management
    ports:
      - "5672:5672"
      - "15672:15672"

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf

volumes:
  mysql_data:
```

### 6.3 Kubernetes 线上部署（生产建议）

每个微服务打包为 Docker 镜像，通过 Nacos 进行服务注册与发现。

---

## 7. 项目结构

```
dafuweng
├── dafuweng-gateway/          # 网关服务
├── dafuweng-auth/             # 认证服务
├── dafuweng-sales/            # 销售服务
├── dafuweng-finance/          # 金融服区
├── dafuweng-system/           # 系统管理
├── dafuweng-notify/            # 消息通知
├── dafuweng-common/           # 公共模块
│   ├── common-core/            # 实体/枚举/异常
│   ├── common-mybatis/         # MyBatisPlus配置/拦截器
│   ├── common-redis/           # Redis配置
│   └── common-feign/           # Feign配置/拦截器
├── nacos-config/              # Nacos配置文件
├── scripts/                   # SQL脚本/部署脚本
└── docker-compose.yml
```

---

## 8. 实施计划

### Phase 1: 基础设施（1周）
- [ ] 搭建本地 Docker 环境（MySQL/Redis/Nacos/RabbitMQ/Nginx）
- [ ] 初始化 Nacos 配置中心（各服务配置文件）
- [ ] 创建公共模块 dafuweng-common（实体基类/统一响应/异常处理）
- [ ] 搭建网关服务（路由规则/鉴权拦截器）

### Phase 2: 认证授权（1周）
- [ ] 搭建 auth-service（账号/角色/权限）
- [ ] 集成 JWT（登录/刷新/登出）
- [ ] 集成 Redis（Token黑名单/登录限流）
- [ ] 数据权限拦截器（MyBatisPlus）

### Phase 3: 销售核心（2周）
- [ ] 客户管理（增删改查/查重/公海/迁移/批注）
- [ ] 洽谈记录
- [ ] 合同管理（创建/签署/附件上传/状态流转）
- [ ] 工作日志
- [ ] 销售业绩统计

### Phase 4: 金融核心（2周）
- [ ] 金融产品管理
- [ ] 贷款审核流程（接收合同/初审/提交银行/接收结果）
- [ ] 服务费管理（收取记录）
- [ ] 业绩计算与排名

### Phase 5: 系统管理与通知（1周）
- [ ] 部门管理（增删改/战区）
- [ ] 账号管理（系统管理员）
- [ ] 操作日志
- [ ] RabbitMQ 消息队列集成
- [ ] 定时任务（客户入公海/业绩月报/对账）

### Phase 6: 集成测试与部署（1周）
- [ ] OpenFeign 服务间调用联调
- [ ] 全链路鉴权测试
- [ ] Nginx 静态资源与负载均衡配置
- [ ] 预发布环境验证
- [ ] 生产部署文档

---

## 9. NOT in scope

- 银行系统直连对接（仅记录银行反馈，不做银行API集成）
- 移动端App原生开发（仅支持移动端H5/小程序）
- 贷款合同电子签章（纸质合同+扫描件）
- 财务对账自动化（手动对账+记录）
- 数据分析/BI报表（仅基础统计功能）
- 外部征信对接

---

## 10. 关键风险

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| 客户查重不准 | 同一客户被多人录入 | 手机号+姓名联合唯一索引，录入时强制查重 |
| 公海客户被重复领取 | 数据竞争 | Redisson分布式锁，领取操作加锁 |
| 合同状态机错乱 | 业务数据不一致 | 状态流转用有限状态机+事件溯源 |
| 银行反馈丢失 | 贷款状态不准确 | RabbitMQ持久化+手动重推机制 |
| 业绩计算并发 | 同一合同重复计算 | Redisson锁+幂等计算 |

---

## 11. 测试策略

### 单元测试
- Service层：每个业务方法有对应测试类
- MyBatisPlus拦截器：数据权限/逻辑删除拦截测试

### 集成测试
- Feign调用：模拟服务间通信
- RabbitMQ：消息发送/消费完整链路
- 数据库：事务一致性测试

### E2E测试（手工）
- 完整业务流程：录入客户→洽谈→签约→审核→放款

---

## 12. 关键实现要点

### 12.1 客户查重流程

```
销售代表录入客户
      ↓
前端调用 POST /customer/check 查重（姓名+手机号）
      ↓
后端查 MySQL: SELECT * FROM customer WHERE name=? AND phone=? AND deleted=0
      ↓
  已存在 → 返回已有客户信息，提示"该客户已存在，由[XXX]录入"
  不存在 → 允许新增
```

### 12.2 公海自动释放（RabbitMQ DLX延迟队列）

```
定时扫描（每天凌晨2点）：
  SELECT * FROM customer
  WHERE status NOT IN ('已签约','已放款','已完成')
  AND created_at < NOW() - INTERVAL N DAY
  AND public_sea_time IS NULL

找到后：
  发送消息到 RabbitMQ 延迟队列（延迟N天）
  → 消费者收到消息后执行 UPDATE customer SET status='公海', public_sea_time=NOW()
```

### 12.3 合同状态机

```
                    ┌─── 草稿(draft)
                    │
  创建合同 ──→ 签署(sign) ──→ 支付首期(first-fee) ──→ 审核中(audit)
                                                              │
                                                         ┌────┴────┐
                                                         ↓         ↓
                                                     已通过    已拒绝
                                                     (approved)  (rejected)
                                                         │
                                                    放款(grant) ──→ 已放款(granted)
                                                         │
                                                    支付二期(second-fee) ──→ 已完成(completed)
```

### 12.4 分布式锁示例（Redisson）

```java
// 领取公海客户
RLock lock = redissonClient.getLock("customer:claim:" + customerId);
try {
    if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
        // 检查客户是否还在公海
        // 领取操作
    }
} finally {
    lock.unlock();
}
```
