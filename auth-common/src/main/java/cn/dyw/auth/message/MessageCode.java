package cn.dyw.auth.message;

import lombok.Getter;

/**
 * @author dyw770
 * @since 2025-03-05
 */
public enum MessageCode {

    SUCCESS(0, "请求成功"),

    ERROR(1, "请求失败"),

    // 1000 - 1100 登录相关，这部分错误码前台需要直接退出
    NOT_LOGIN(1000, "未登录"),
    TOKEN_ERROR(1002, "token错误"),
    TOKEN_INVALID(1003, "token无效或已过期"),
    TOKEN_NOT_SET(1004, "token未设置"),

    BAD_CREDENTIALS(1100, "用户名或密码错误"),
    
    PARAM_ERROR(2000, "参数错误"),
    
    REQUEST_METHOD_ERROR(2003, "请求方法错误"),
    REQUEST_BODY_ERROR(2004, "Request Body异常"),

    // 3000 权限相关
    NOT_PERMISSION(3001, "没有权限"),
    
    SQL_CHECK_ERROR(4000, "SQL校验异常"),
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
