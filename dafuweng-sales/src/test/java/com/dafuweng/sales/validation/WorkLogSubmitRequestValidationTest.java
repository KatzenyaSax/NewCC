package com.dafuweng.sales.validation;

import com.dafuweng.sales.domain.dto.WorkLogSubmitRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WorkLogSubmitRequest 参数校验测试")
class WorkLogSubmitRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<WorkLogSubmitRequest>> validate(WorkLogSubmitRequest request) {
        return validator.validate(request);
    }

    @Nested
    @DisplayName("必填字段校验")
    class RequiredFields {

        @Test
        @DisplayName("缺少 logDate - 应返回'日志日期不能为空'")
        void logDateNull() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setCallsMade(10);
            req.setEffectiveCalls(5);
            req.setIntentionClients(2);
            req.setFaceToFaceClients(1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("日志日期不能为空")));
        }

        @Test
        @DisplayName("缺少 callsMade - 应返回'打电话数不能为空'")
        void callsMadeNull() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setLogDate(LocalDate.now());
            req.setEffectiveCalls(5);
            req.setIntentionClients(2);
            req.setFaceToFaceClients(1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("打电话数不能为空")));
        }

        @Test
        @DisplayName("缺少 effectiveCalls - 应返回'有效电话数不能为空'")
        void effectiveCallsNull() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setLogDate(LocalDate.now());
            req.setCallsMade(10);
            req.setIntentionClients(2);
            req.setFaceToFaceClients(1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("有效电话数不能为空")));
        }

        @Test
        @DisplayName("缺少 intentionClients - 应返回'意向客户数不能为空'")
        void intentionClientsNull() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setLogDate(LocalDate.now());
            req.setCallsMade(10);
            req.setEffectiveCalls(5);
            req.setFaceToFaceClients(1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向客户数不能为空")));
        }

        @Test
        @DisplayName("缺少 faceToFaceClients - 应返回'面谈客户数不能为空'")
        void faceToFaceClientsNull() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setLogDate(LocalDate.now());
            req.setCallsMade(10);
            req.setEffectiveCalls(5);
            req.setIntentionClients(2);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("面谈客户数不能为空")));
        }
    }

    @Nested
    @DisplayName("打电话数非负校验")
    class CallsMadeNonNegative {

        @Test
        @DisplayName("callsMade为-1 - 返回'打电话数不能为负'")
        void callsMadeNegative() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setCallsMade(-1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("打电话数不能为负")));
        }

        @Test
        @DisplayName("callsMade为0 - 验证通过")
        void callsMadeZero() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setCallsMade(0);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("callsMade为100 - 验证通过")
        void callsMadeLarge() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setCallsMade(100);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("有效电话数非负校验")
    class EffectiveCallsNonNegative {

        @Test
        @DisplayName("effectiveCalls为-1 - 返回'有效电话数不能为负'")
        void effectiveCallsNegative() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setEffectiveCalls(-1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("有效电话数不能为负")));
        }

        @Test
        @DisplayName("effectiveCalls超过callsMade - 验证通过（业务上可能不合理但技术上合法）")
        void effectiveCallsGreaterThanCalls() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setCallsMade(5);
            req.setEffectiveCalls(10);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("意向客户数非负校验")
    class IntentionClientsNonNegative {

        @Test
        @DisplayName("intentionClients为-1 - 返回'意向客户数不能为负'")
        void intentionClientsNegative() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setIntentionClients(-1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向客户数不能为负")));
        }

        @Test
        @DisplayName("intentionClients为0 - 验证通过")
        void intentionClientsZero() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setIntentionClients(0);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("面谈客户数非负校验")
    class FaceToFaceClientsNonNegative {

        @Test
        @DisplayName("faceToFaceClients为-1 - 返回'面谈客户数不能为负'")
        void faceToFaceClientsNegative() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setFaceToFaceClients(-1);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("面谈客户数不能为负")));
        }

        @Test
        @DisplayName("faceToFaceClients为0 - 验证通过")
        void faceToFaceClientsZero() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setFaceToFaceClients(0);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("备注字段可选")
    class ContentOptional {

        @Test
        @DisplayName("content为null - 验证通过（非必填）")
        void contentNull() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setContent(null);

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("content为空字符串 - 验证通过（非必填）")
        void contentEmpty() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setContent("");

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("content为长文本 - 验证通过（非必填，无长度限制）")
        void contentLong() {
            WorkLogSubmitRequest req = validBaseRequest();
            req.setContent("这是一段很长的备注内容，没有任何长度限制的注解。".repeat(100));

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("全部必填字段合法 - 验证通过")
    class AllFieldsValid {

        @Test
        @DisplayName("全部必填字段合法 - 验证通过")
        void allFieldsValid() {
            WorkLogSubmitRequest req = new WorkLogSubmitRequest();
            req.setLogDate(LocalDate.of(2026, 3, 31));
            req.setCallsMade(20);
            req.setEffectiveCalls(8);
            req.setIntentionClients(3);
            req.setFaceToFaceClients(2);
            req.setContent("今日工作汇报");

            Set<ConstraintViolation<WorkLogSubmitRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    private WorkLogSubmitRequest validBaseRequest() {
        WorkLogSubmitRequest req = new WorkLogSubmitRequest();
        req.setLogDate(LocalDate.now());
        req.setCallsMade(10);
        req.setEffectiveCalls(5);
        req.setIntentionClients(2);
        req.setFaceToFaceClients(1);
        return req;
    }
}
