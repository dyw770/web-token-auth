package cn.dyw.auth.exception;

import cn.dyw.auth.message.MessageCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author dyw770
 * @since 2025-03-05
 */
public class ExtensionBusinessException extends BusinessException {
    
    @Getter
    private final String message;
    
    public ExtensionBusinessException(MessageCode messageCode, String message) {
        super(messageCode);
        this.message = message;
    }
}
