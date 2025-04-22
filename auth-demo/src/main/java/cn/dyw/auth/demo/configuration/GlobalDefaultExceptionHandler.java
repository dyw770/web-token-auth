package cn.dyw.auth.demo.configuration;


import cn.dyw.auth.exception.BusinessException;
import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * 全局异常错误
 *
 * @author litao
 */
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * token解析
     */
    private final TokenResolve tokenResolve;

    private final SecurityTokenRepository tokenRepository;

    public GlobalDefaultExceptionHandler(TokenResolve tokenResolve, SecurityTokenRepository tokenRepository) {
        this.tokenResolve = tokenResolve;
        this.tokenRepository = tokenRepository;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> defaultExceptionHandler(Exception e, HttpServletRequest request) {
        log(e, request);
        return Result.createFail(MessageCode.ERROR, e.getMessage());
    }

    /**
     * token校验失败，匿名无权限时抛出异常
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public Result<Void> insufficientAuthenticationExceptionHandler(Exception e, HttpServletRequest request) {
        log(e, request);

        String tokenStr = tokenResolve.tokenResolve(request);

        if (!StringUtils.hasText(tokenStr)) {
            return Result.createFail(MessageCode.TOKEN_NOT_SET);
        }

        return Result.createFail(MessageCode.TOKEN_INVALID);
    }

    /**
     * 无权限异常
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public Result<Void> authorizationDeniedExceptionExceptionHandler(Exception e, HttpServletRequest request) {
        log(e, request);
        return Result.createFail(MessageCode.NOT_PERMISSION);
    }


    /**
     * 业务异常
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler(ExtensionBusinessException.class)
    public Result<Void> extensionBusinessExceptionHandler(BusinessException e, HttpServletRequest request) {
        log(e, request);

        return Result.createFail(e.getMessageCode().getCode(), e.getMessage(), null);
    }

    /**
     * 业务异常
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> businessExceptionHandler(BusinessException e, HttpServletRequest request) {
        log(e, request);

        return Result.createFail(e.getMessageCode());
    }

    private void log(Exception e, HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("捕获到异常-[{}] ", request.getRequestURI(), e);
        } else {
            log.info("捕获到异常-[{}] \n\t{}", request.getRequestURI(), e.getMessage());
        }
    }
}
