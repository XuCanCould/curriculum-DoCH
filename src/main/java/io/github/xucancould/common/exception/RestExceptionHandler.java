package io.github.xucancould.common.exception;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.exception.HttpException;
import io.github.xucancould.common.configuration.CodeMessageConfiguration;
import io.github.xucancould.vo.UnifyResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.talelin.autoconfigure.util.RequestUtil;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static io.github.talelin.autoconfigure.util.RequestUtil.getSimpleRequest;

/**
 * created by Xu on 2024/3/27 20:45.
 */
@Order
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size:20M}")
    private String maxFileSize;

    /**
     * 处理请求异常。设置错误码，响应状态码，错误信息，打印报错信息
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @ExceptionHandler(HttpException.class)
    public UnifyResponseVO<String> processException(HttpException exception, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));
        int code = exception.getCode();
        unifyResponseVO.setCode(code);
        response.setStatus(exception.getHttpCode());
        String errorMessage = CodeMessageConfiguration.getMessage(code);
        if (!StringUtils.hasText(errorMessage)) {
            unifyResponseVO.setMessage(exception.getMessage());
            log.error("", exception);
        } else {
            unifyResponseVO.setMessage(errorMessage);
            log.error(exception.getClass().getConstructor(int.class, String.class).newInstance(code, errorMessage).toString());
        }
        return unifyResponseVO;
    }

    /**
     * 这里处理的是 org.springframework.validation.BindException;
     * @param exception
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(BindException.class)
    public UnifyResponseVO<Map<String, Object>> processException(BindException exception,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response) {
        log.error(exception.toString());
        Map<String, Object> msg = new HashMap<>(3);
        exception.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                // 前面有用这个包下的StringUtils（org.springframework.util.StringUtils），使用其他的需要全类名导入
                msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldError.getField()),
                        fieldError.getDefaultMessage());
            } else {
                msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(error.getObjectName()),
                        error.getDefaultMessage());
            }
        });
        return getMapUnifyResponseVO(request, response, msg);
    }

    /**
     * 普通参数(非 java bean)校验出错时抛出 ConstraintViolationException 异常
     * 同样是参数异常，但不同于 validation
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public UnifyResponseVO<Map<String, Object>> processException(ConstraintViolationException exception,
                                                                                              HttpServletRequest request,
                                                                                              HttpServletResponse response) {
        log.error(exception.toString());
        Map<String, Object> msg = new HashMap<>(3);
        exception.getConstraintViolations().forEach(error -> {
            String message = error.getMessage();
            String path = error.getPropertyPath().toString();
            msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(path), message);
        });
        return getMapUnifyResponseVO(request, response, msg);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public UnifyResponseVO<String> processException(NoHandlerFoundException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));
        String message = CodeMessageConfiguration.getMessage(10025);
        if (!StringUtils.hasText(message)) {
            unifyResponseVO.setMessage(exception.getMessage());
        } else {
            unifyResponseVO.setMessage(message);
        }
        unifyResponseVO.setCode(Code.NOT_FOUND.getCode());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return unifyResponseVO;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public UnifyResponseVO<String> processException(MissingServletRequestParameterException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));

        String errorMessage = CodeMessageConfiguration.getMessage(10150);
        if (!StringUtils.hasText(errorMessage)) {
            unifyResponseVO.setMessage(exception.getMessage());
        } else {
            unifyResponseVO.setMessage(errorMessage + exception.getParameterName());
        }
        unifyResponseVO.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
   }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public UnifyResponseVO<String> processException(MethodArgumentTypeMismatchException exception,
                                                                                 HttpServletRequest request,
                                                                                 HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO<String> result = new UnifyResponseVO<>();
        result.setRequest(getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10160);
        if (!StringUtils.hasText(errorMessage)) {
            result.setMessage(exception.getMessage());
        } else {
            result.setMessage(exception.getValue() + errorMessage);
        }
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    @ExceptionHandler(ServletException.class)
    public UnifyResponseVO<String> processException(ServletException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));
        unifyResponseVO.setMessage(exception.getMessage());
        unifyResponseVO.setCode(Code.FAIL.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public UnifyResponseVO<String> processException(HttpMessageNotReadableException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10170);
        Throwable cause = exception.getCause();
        if (cause != null) {
            String msg = convertMessage(cause);
            unifyResponseVO.setMessage(msg);
        } else {
            if (!StringUtils.hasText(errorMessage)) {
                unifyResponseVO.setMessage(exception.getMessage());
            } else {
                unifyResponseVO.setMessage(errorMessage);
            }
        }
        unifyResponseVO.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
    }

    @ExceptionHandler(TypeMismatchException.class)
    public UnifyResponseVO<String> handleTypeMismatchException(TypeMismatchException exception,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(getSimpleRequest(request));
        unifyResponseVO.setMessage(exception.getMessage());
        unifyResponseVO.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public UnifyResponseVO<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception,
                                                                         HttpServletRequest request,
                                                                         HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10180);
        if (!StringUtils.hasText(errorMessage)) {
            unifyResponseVO.setMessage(exception.getMessage());
        } else {
            unifyResponseVO.setMessage(errorMessage + maxFileSize);
        }
        unifyResponseVO.setCode(Code.FILE_TOO_LARGE.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
    }

    /**
     * 捕获所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public UnifyResponseVO<String> processException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        log.error(exception.toString());
        UnifyResponseVO<String> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(getSimpleRequest(request));
        unifyResponseVO.setMessage(Code.INTERNAL_SERVER_ERROR.getZhDescription());
        unifyResponseVO.setCode(Code.INTERNAL_SERVER_ERROR.getCode());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return unifyResponseVO;
    }

    /**
     * 当出现参数类型的错误时，转化错误信息
     * @param cause
     * @return
     */
    private String convertMessage(Throwable cause) {
        String error = cause.toString();
        String regulation = "\\[\"(.*?)\"]+";
        Pattern pattern = Pattern.compile(regulation);
        Matcher matcher = pattern.matcher(error);
        String group = "";
        if (matcher.find()) {
            String matchString = matcher.group();
            matchString = matchString
                    .replace("[", "")
                    .replace("]", "");
            matchString = matchString.replaceAll("\\\"", "") + "字段类型错误";
            group += matchString;
        }
        return group;
    }

    private UnifyResponseVO<Map<String, Object>> getMapUnifyResponseVO(HttpServletRequest request,
                                                                       HttpServletResponse response,
                                                                       Map<String, Object> msg) {
        UnifyResponseVO<Map<String, Object>> unifyResponseVO = new UnifyResponseVO<>();
        unifyResponseVO.setRequest(RequestUtil.getSimpleRequest(request));
        unifyResponseVO.setMessage(msg);
        unifyResponseVO.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponseVO;
    }

}
