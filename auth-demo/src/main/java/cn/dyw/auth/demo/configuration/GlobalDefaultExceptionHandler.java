package cn.dyw.auth.demo.configuration;


import cn.dyw.auth.exception.BusinessException;
import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Iterator;
import java.util.Optional;

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

    /**
     * 默认错误处理
     *
     * @param e       异常
     * @param request 请求信息
     * @return 响应
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> defaultExceptionHandler(Exception e, HttpServletRequest request) {
        log(e, request);
        return Result.createFail(MessageCode.ERROR, e.getMessage());
    }

    /**
     * 用户名密码错误
     *
     * @param e       异常
     * @param request 请求信息
     * @return 响应
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result<Void> badCredentialsExceptionHandler(Exception e, HttpServletRequest request) {
        log(e, request);
        return Result.createFail(MessageCode.BAD_CREDENTIALS);
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
     * 参数相关异常
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            ServletRequestBindingException.class,
            MethodArgumentTypeMismatchException.class})
    private Result<Void> argumentExceptionHandler(Exception e,
                                                  HttpServletRequest request) {
        log(e, request);

        if (e instanceof ConstraintViolationException ex) {
            Optional<ConstraintViolation<?>> first = ex.getConstraintViolations().stream().findFirst();
            if (first.isPresent()) {
                ConstraintViolation<?> constraintViolation = first.get();
                Iterator<Path.Node> iterator = constraintViolation.getPropertyPath().iterator();
                String fieldName = "";
                while (iterator.hasNext()) {
                    fieldName = iterator.next().getName();
                }
                return Result.createFailWithMsg(MessageCode.PARAM_ERROR, buildValidMessage(fieldName, constraintViolation.getMessage()));
            }
        }

        if (e instanceof MethodArgumentNotValidException ex) {
            Optional<FieldError> first = ex.getBindingResult().getFieldErrors().stream().findFirst();
            if (first.isPresent()) {
                FieldError fieldError = first.get();
                return Result.createFailWithMsg(MessageCode.PARAM_ERROR, buildValidMessage(fieldError.getField(), fieldError.getDefaultMessage()));
            }
        }

        if (e instanceof MethodArgumentTypeMismatchException ex) {
            if (ex.getRequiredType() != null) {
                return Result.createFailWithMsg(MessageCode.PARAM_ERROR, buildValidMessage(ex.getPropertyName(), "request type " + ex.getRequiredType().getName()));
            }
        }

        return Result.createFailWithMsg(MessageCode.PARAM_ERROR, e.getMessage());
    }

    /**
     * 请求方法错误
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private Result<Void> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e,
                                                                       HttpServletRequest request) {
        log(e, request);
        return Result.createFail(MessageCode.REQUEST_METHOD_ERROR);
    }

    /**
     * 缺少 Request body
     *
     * @param e       异常
     * @param request 请求
     * @return 响应
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    private Result<Void> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e,
                                                                HttpServletRequest request) {
        log(e, request);
        return Result.createFailWithMsg(MessageCode.REQUEST_BODY_ERROR, e.getMessage());
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

    private String buildValidMessage(String field, String message) {
        return field + ": " + message;
    }
}
