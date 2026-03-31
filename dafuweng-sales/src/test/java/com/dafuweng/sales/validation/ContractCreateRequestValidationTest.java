package com.dafuweng.sales.validation;

import com.dafuweng.sales.domain.dto.ContractCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContractCreateRequest 参数校验测试")
class ContractCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<ContractCreateRequest>> validate(ContractCreateRequest request) {
        return validator.validate(request);
    }

    @Nested
    @DisplayName("必填字段校验")
    class RequiredFields {

        @Test
        @DisplayName("缺少 customerId - 应返回'客户ID不能为空'")
        void customerIdNull() {
            ContractCreateRequest req = new ContractCreateRequest();
            req.setContractAmount(new BigDecimal("10000"));
            req.setServiceFeeRate(new BigDecimal("0.05"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户ID不能为空")));
        }

        @Test
        @DisplayName("缺少 contractAmount - 应返回'合同金额不能为空'")
        void contractAmountNull() {
            ContractCreateRequest req = new ContractCreateRequest();
            req.setCustomerId(1L);
            req.setServiceFeeRate(new BigDecimal("0.05"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("合同金额不能为空")));
        }

        @Test
        @DisplayName("缺少 serviceFeeRate - 应返回'服务费率不能为空'")
        void serviceFeeRateNull() {
            ContractCreateRequest req = new ContractCreateRequest();
            req.setCustomerId(1L);
            req.setContractAmount(new BigDecimal("10000"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("服务费率不能为空")));
        }
    }

    @Nested
    @DisplayName("合同金额校验")
    class ContractAmountRange {

        @Test
        @DisplayName("合同金额为0 - 返回'合同金额必须大于0'")
        void contractAmountZero() {
            ContractCreateRequest req = validBaseRequest();
            req.setContractAmount(BigDecimal.ZERO);

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("合同金额必须大于0")));
        }

        @Test
        @DisplayName("合同金额为负数 - 返回'合同金额必须大于0'")
        void contractAmountNegative() {
            ContractCreateRequest req = validBaseRequest();
            req.setContractAmount(new BigDecimal("-1000"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("合同金额必须大于0")));
        }

        @Test
        @DisplayName("合同金额为0.01 - 验证通过")
        void contractAmountMinValid() {
            ContractCreateRequest req = validBaseRequest();
            req.setContractAmount(new BigDecimal("0.01"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("合同金额为大数额 - 验证通过")
        void contractAmountLarge() {
            ContractCreateRequest req = validBaseRequest();
            req.setContractAmount(new BigDecimal("100000000"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("服务费率校验")
    class ServiceFeeRateRange {

        @Test
        @DisplayName("服务费率为负数 - 返回'服务费率不能为负'")
        void serviceFeeRateNegative() {
            ContractCreateRequest req = validBaseRequest();
            req.setServiceFeeRate(new BigDecimal("-0.01"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("服务费率不能为负")));
        }

        @Test
        @DisplayName("服务费率为1.01 - 返回'服务费率不能大于1'")
        void serviceFeeRateOverOne() {
            ContractCreateRequest req = validBaseRequest();
            req.setServiceFeeRate(new BigDecimal("1.01"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("服务费率不能大于1")));
        }

        @Test
        @DisplayName("服务费率为0 - 验证通过")
        void serviceFeeRateZero() {
            ContractCreateRequest req = validBaseRequest();
            req.setServiceFeeRate(BigDecimal.ZERO);

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("服务费率为1 - 验证通过")
        void serviceFeeRateExactlyOne() {
            ContractCreateRequest req = validBaseRequest();
            req.setServiceFeeRate(BigDecimal.ONE);

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("服务费率为0.05 - 验证通过")
        void serviceFeeRateValid() {
            ContractCreateRequest req = validBaseRequest();
            req.setServiceFeeRate(new BigDecimal("0.05"));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("字段长度校验")
    class SizeLimits {

        @Test
        @DisplayName("loanUse超过200字符 - 返回'贷款用途最多200字符'")
        void loanUseTooLong() {
            ContractCreateRequest req = validBaseRequest();
            req.setLoanUse("贷款用途".repeat(201));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("贷款用途最多200字符")));
        }

        @Test
        @DisplayName("remark超过500字符 - 返回'备注最多500字符'")
        void remarkTooLong() {
            ContractCreateRequest req = validBaseRequest();
            req.setRemark("备注".repeat(501));

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("备注最多500字符")));
        }
    }

    @Nested
    @DisplayName("全部字段合法 - 验证通过")
    class AllFieldsValid {

        @Test
        @DisplayName("全部必填字段合法 - 验证通过")
        void allFieldsValid() {
            ContractCreateRequest req = new ContractCreateRequest();
            req.setCustomerId(1L);
            req.setContractAmount(new BigDecimal("50000"));
            req.setServiceFeeRate(new BigDecimal("0.03"));
            req.setLoanUse("购车");
            req.setGuaranteeInfo("{}");
            req.setRemark("客户沟通顺利");

            Set<ConstraintViolation<ContractCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    private ContractCreateRequest validBaseRequest() {
        ContractCreateRequest req = new ContractCreateRequest();
        req.setCustomerId(1L);
        req.setContractAmount(new BigDecimal("10000"));
        req.setServiceFeeRate(new BigDecimal("0.05"));
        return req;
    }
}
