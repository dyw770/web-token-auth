package cn.dyw.auth.exception;

import cn.dyw.auth.message.MessageCode;
import lombok.Getter;

/**
 * 业务异常
 * 
 * @author dyw770
 * @since 2025-03-05
 */
public class BusinessException extends RuntimeException {
    
    @Getter
    private final MessageCode messageCode;
    
    public BusinessException(MessageCode messageCode) {
        this.messageCode = messageCode;
    }
}
