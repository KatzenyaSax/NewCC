package com.dafuweng.common.core.result;

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

    public static <T> R<T> error(com.dafuweng.common.core.enums.ErrorCode errorCode) {
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
