package cn.dyw.auth.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private int code;

    private String msg;

    private boolean success;

    private T data;

    public Result(int code, String msg, boolean b, T data) {
        this.code = code;
        this.msg = msg;
        this.success = b;
        this.data = data;
    }


    /**
     * 快速返回一个成功操作
     *
     * @param msg msg
     * @param data data
     * @param <K> K
     * @return Result
     */
    public static <K> Result<K> createSuccess(String msg, K data) {
        return new Result<>(MessageCode.SUCCESS.getCode(), msg, true, data);
    }

    public static <K> Result<K> createSuccess(K data) {
        return new Result<>(MessageCode.SUCCESS.getCode(), MessageCode.SUCCESS.getMessage(), true, data);
    }

    public static Result<Void> createSuccess() {
        return new Result<>(MessageCode.SUCCESS.getCode(), MessageCode.SUCCESS.getMessage(), true, null);
    }

    public static <K> Result<K> createFail(int code, String msg, K data) {
        return new Result<>(code, msg, false, data);
    }

    public static <K> Result<K> createFail(MessageCode code, K data) {
        return new Result<>(code.getCode(), code.getMessage(), false, data);
    }

    public static <K> Result<K> createFail(MessageCode code, String msg, K data) {
        return new Result<>(code.getCode(), msg, false, data);
    }

    public static Result<Void> createFailWithMsg(MessageCode code, String msg) {
        return new Result<>(code.getCode(), msg, false, null);
    }
    
    public static Result<Void> createFail(MessageCode code) {
        return new Result<>(code.getCode(), code.getMessage(), false, null);
    }
}
