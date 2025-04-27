package cn.dyw.auth.support;

import java.util.Arrays;

/**
 * @param message      日志模版
 * @param module       日志模块
 * @param args         spel执行结果
 * @param throwable 异常信息
 * @author dyw770
 * @since 2025-04-27
 */
public record SystemEventModel(
        String message,
        String module,
        String[] args,
        String throwable) {


    @Override
    public String toString() {
        return "SystemEventModel{" +
                "message='" + message + '\'' +
                ", module='" + module + '\'' +
                ", args=" + Arrays.toString(args) +
                ", exceptionMsg='" + throwable + '\'' +
                '}';
    }
}
