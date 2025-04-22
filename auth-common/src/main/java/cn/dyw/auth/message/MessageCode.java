package cn.dyw.auth.message;

import lombok.Getter;

/**
 * @author dyw770
 * @since 2025-03-05
 */
public enum MessageCode {

    SUCCESS(0, "请求成功"),

    ERROR(1, "请求失败"),

    NOT_LOGIN(1000, "未登录"),
    NOT_PERMISSION(1001, "没有权限"),
    TOKEN_ERROR(1002, "token错误"),
    TOKEN_INVALID(1003, "token无效或已过期"),
    TOKEN_NOT_SET(1004, "token未设置"),
    
    PARAM_ERROR(2000, "参数错误"),
    ;


    /**
     * 状态码
     */
    @Getter
    private final int code;

    /**
     * 描述信息
     */
    @Getter
    private final String message;


    MessageCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
