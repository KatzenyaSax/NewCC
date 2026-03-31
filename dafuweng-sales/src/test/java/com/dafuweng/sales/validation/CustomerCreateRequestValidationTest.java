package com.dafuweng.sales.validation;

import com.dafuweng.sales.domain.dto.CustomerCreateRequest;
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

@DisplayName("CustomerCreateRequest 参数校验测试")
class CustomerCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<CustomerCreateRequest>> validate(CustomerCreateRequest request) {
        return validator.validate(request);
    }

    @Nested
    @DisplayName("必填字段校验")
    class RequiredFields {

        @Test
        @DisplayName("缺少 name - 应返回'客户姓名不能为空'")
        void nameBlank() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setPhone("13812345678");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户姓名不能为空")));
        }

        @Test
        @DisplayName("缺少 phone - 应返回'手机号不能为空'")
        void phoneBlank() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("手机号不能为空")));
        }

        @Test
        @DisplayName("缺少 customerType - 应返回'客户类型不能为空'")
        void customerTypeNull() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户类型不能为空")));
        }
    }

    @Nested
    @DisplayName("手机号格式校验")
    class PhonePattern {

        @Test
        @DisplayName("手机号格式正确 - 验证通过")
        void validPhone() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("手机号过短 - 返回'手机号格式不正确'")
        void phoneTooShort() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("1381234567"); // 10位
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("手机号格式不正确")));
        }

        @Test
        @DisplayName("手机号以错误前缀开头 - 返回'手机号格式不正确'")
        void phoneWrongPrefix() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("12812345678"); // 以12开头
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("手机号格式不正确")));
        }

        @Test
        @DisplayName("手机号包含字母 - 返回'手机号格式不正确'")
        void phoneWithLetters() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("1381234567a");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("手机号格式不正确")));
        }
    }

    @Nested
    @DisplayName("客户类型校验")
    class CustomerTypeRange {

        @Test
        @DisplayName("客户类型为0 - 返回'客户类型不合法'")
        void customerTypeZero() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(0);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户类型不合法")));
        }

        @Test
        @DisplayName("客户类型为3 - 返回'客户类型不合法'")
        void customerTypeOutOfRange() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(3);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户类型不合法")));
        }

        @Test
        @DisplayName("客户类型为1 - 验证通过")
        void customerTypePersonal() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("客户类型为2 - 验证通过")
        void customerTypeEnterprise() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(2);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("字段长度校验")
    class SizeLimits {

        @Test
        @DisplayName("name超过100字符 - 返回'客户姓名最多100字符'")
        void nameTooLong() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张".repeat(101));
            req.setPhone("13812345678");
            req.setCustomerType(1);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户姓名最多100字符")));
        }

        @Test
        @DisplayName("idCard超过20字符 - 返回'身份证号最多20字符'")
        void idCardTooLong() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(1);
            req.setIdCard("1".repeat(21));

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("身份证号最多20字符")));
        }

        @Test
        @DisplayName("companyName超过200字符 - 返回'企业名称最多200字符'")
        void companyNameTooLong() {
            CustomerCreateRequest req = new CustomerCreateRequest();
            req.setName("张三");
            req.setPhone("13812345678");
            req.setCustomerType(2);
            req.setCompanyName("公司".repeat(201));

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("企业名称最多200字符")));
        }
    }

    @Nested
    @DisplayName("意向等级校验")
    class IntentionLevelRange {

        @Test
        @DisplayName("意向等级为0 - 返回'意向等级不合法'")
        void intentionLevelZero() {
            CustomerCreateRequest req = validBaseRequest();
            req.setIntentionLevel(0);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向等级不合法")));
        }

        @Test
        @DisplayName("意向等级为5 - 返回'意向等级不合法'")
        void intentionLevelOutOfRange() {
            CustomerCreateRequest req = validBaseRequest();
            req.setIntentionLevel(5);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向等级不合法")));
        }

        @Test
        @DisplayName("意向等级为1-4 - 验证通过")
        void intentionLevelValid() {
            CustomerCreateRequest req = validBaseRequest();
            req.setIntentionLevel(3);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("意向金额校验")
    class LoanIntentionAmount {

        @Test
        @DisplayName("意向金额为负数 - 返回'意向金额不能为负'")
        void amountNegative() {
            CustomerCreateRequest req = validBaseRequest();
            req.setLoanIntentionAmount(new BigDecimal("-1"));

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向金额不能为负")));
        }

        @Test
        @DisplayName("意向金额为0 - 验证通过")
        void amountZero() {
            CustomerCreateRequest req = validBaseRequest();
            req.setLoanIntentionAmount(BigDecimal.ZERO);

            Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("全部字段合法 - 验证通过")
    void allFieldsValid() {
        CustomerCreateRequest req = new CustomerCreateRequest();
        req.setName("张三");
        req.setPhone("13812345678");
        req.setCustomerType(1);
        req.setIdCard("110101199001011234");
        req.setIntentionLevel(2);
        req.setLoanIntentionAmount(new BigDecimal("500000"));
        req.setLoanIntentionProduct("抵押贷");
        req.setSource("网络推广");
        req.setCompanyName("测试公司");
        req.setCompanyLegalPerson("李四");

        Set<ConstraintViolation<CustomerCreateRequest>> violations = validate(req);
        assertTrue(violations.isEmpty());
    }

    private CustomerCreateRequest validBaseRequest() {
        CustomerCreateRequest req = new CustomerCreateRequest();
        req.setName("张三");
        req.setPhone("13812345678");
        req.setCustomerType(1);
        return req;
    }
}
