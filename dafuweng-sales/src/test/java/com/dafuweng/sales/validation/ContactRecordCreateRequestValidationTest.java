package com.dafuweng.sales.validation;

import com.dafuweng.sales.domain.dto.ContactRecordCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContactRecordCreateRequest 参数校验测试")
class ContactRecordCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<ContactRecordCreateRequest>> validate(ContactRecordCreateRequest request) {
        return validator.validate(request);
    }

    @Nested
    @DisplayName("必填字段校验")
    class RequiredFields {

        @Test
        @DisplayName("缺少 customerId - 应返回'客户ID不能为空'")
        void customerIdNull() {
            ContactRecordCreateRequest req = new ContactRecordCreateRequest();
            req.setContactType(1);
            req.setContactDate(LocalDateTime.now());
            req.setContent("洽谈内容");

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("客户ID不能为空")));
        }

        @Test
        @DisplayName("缺少 contactType - 应返回'联系类型不能为空'")
        void contactTypeNull() {
            ContactRecordCreateRequest req = new ContactRecordCreateRequest();
            req.setCustomerId(1L);
            req.setContactDate(LocalDateTime.now());
            req.setContent("洽谈内容");

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("联系类型不能为空")));
        }

        @Test
        @DisplayName("缺少 contactDate - 应返回'联系时间不能为空'")
        void contactDateNull() {
            ContactRecordCreateRequest req = new ContactRecordCreateRequest();
            req.setCustomerId(1L);
            req.setContactType(1);
            req.setContent("洽谈内容");

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("联系时间不能为空")));
        }

        @Test
        @DisplayName("缺少 content - 应返回'洽谈内容不能为空'")
        void contentBlank() {
            ContactRecordCreateRequest req = new ContactRecordCreateRequest();
            req.setCustomerId(1L);
            req.setContactType(1);
            req.setContactDate(LocalDateTime.now());

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("洽谈内容不能为空")));
        }
    }

    @Nested
    @DisplayName("联系类型范围校验")
    class ContactTypeRange {

        @Test
        @DisplayName("联系类型为0 - 返回'联系类型不合法'")
        void contactTypeZero() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContactType(0);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("联系类型不合法")));
        }

        @Test
        @DisplayName("联系类型为4 - 返回'联系类型不合法'")
        void contactTypeOutOfRange() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContactType(4);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("联系类型不合法")));
        }

        @Test
        @DisplayName("联系类型为1（电话）- 验证通过")
        void contactTypePhone() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContactType(1);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("联系类型为2（面谈）- 验证通过")
        void contactTypeFaceToFace() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContactType(2);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("联系类型为3（转介绍）- 验证通过")
        void contactTypeReferral() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContactType(3);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("洽谈内容长度校验")
    class ContentSizeLimit {

        @Test
        @DisplayName("洽谈内容超过2000字符 - 返回'洽谈内容最多2000字符'")
        void contentTooLong() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContent("内容".repeat(2001));

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("洽谈内容最多2000字符")));
        }

        @Test
        @DisplayName("洽谈内容正好2000字符 - 验证通过")
        void contentExactly2000() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setContent("内容".repeat(2000));

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("意向等级范围校验")
    class IntentionLevelRange {

        @Test
        @DisplayName("洽谈前意向等级为0 - 返回'意向等级不合法'")
        void intentionBeforeZero() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setIntentionBefore(0);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向等级不合法")));
        }

        @Test
        @DisplayName("洽谈后意向等级为5 - 返回'意向等级不合法'")
        void intentionAfterOutOfRange() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setIntentionAfter(5);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("意向等级不合法")));
        }

        @Test
        @DisplayName("洽谈前后意向等级为1-4 - 验证通过")
        void intentionLevelsValid() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setIntentionBefore(2);
            req.setIntentionAfter(3);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("意向等级为空 - 验证通过（非必填）")
        void intentionLevelNull() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setIntentionBefore(null);
            req.setIntentionAfter(null);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("可选字段 - 均可为空")
    class OptionalFields {

        @Test
        @DisplayName("followUpDate可为空")
        void followUpDateNull() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setFollowUpDate(null);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("attachmentUrls可为空")
        void attachmentUrlsNull() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setAttachmentUrls(null);

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("attachmentUrls可为空列表")
        void attachmentUrlsEmpty() {
            ContactRecordCreateRequest req = validBaseRequest();
            req.setAttachmentUrls(List.of());

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("全部字段合法 - 验证通过")
    class AllFieldsValid {

        @Test
        @DisplayName("全部字段合法 - 验证通过")
        void allFieldsValid() {
            ContactRecordCreateRequest req = new ContactRecordCreateRequest();
            req.setCustomerId(1L);
            req.setContactType(1);
            req.setContactDate(LocalDateTime.of(2026, 3, 31, 10, 30));
            req.setContent("客户对产品有兴趣，约定下周再次沟通");
            req.setIntentionBefore(2);
            req.setIntentionAfter(3);
            req.setFollowUpDate(LocalDate.of(2026, 4, 7));
            req.setAttachmentUrls(List.of("https://example.com/file1.pdf"));

            Set<ConstraintViolation<ContactRecordCreateRequest>> violations = validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    private ContactRecordCreateRequest validBaseRequest() {
        ContactRecordCreateRequest req = new ContactRecordCreateRequest();
        req.setCustomerId(1L);
        req.setContactType(1);
        req.setContactDate(LocalDateTime.now());
        req.setContent("洽谈内容");
        return req;
    }
}
