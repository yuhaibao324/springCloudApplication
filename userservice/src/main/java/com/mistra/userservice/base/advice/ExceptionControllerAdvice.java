package com.mistra.userservice.base.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mistra.userservice.base.exception.BusinessCodeMessage;
import com.mistra.userservice.base.exception.BusinessException;
import com.mistra.userservice.base.exception.ExceptionAdvice;
import com.mistra.userservice.base.i18n.InternationalizationUtil;
import com.mistra.userservice.base.result.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangRui
 * @Date: 2018/12/20
 * Time: 17:55
 * Description:
 */
@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @Autowired
    private InternationalizationUtil i18nUtil;

    /**
     * 处理BusinessException异常返回信息
     *
     * @param businessException
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(BusinessException businessException) {
        String errorCode = businessException.getMessage();
        if (StringUtils.isEmpty(errorCode)) {
            errorCode = String.valueOf(SystemErrorCode.SYSTEM_ERROR);
        }
        String resultMessage = i18nUtil.i18n(errorCode);
        logger.info("业务异常:{} >>> {}", errorCode, resultMessage);
        return new Result(Integer.valueOf(errorCode), resultMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object handle(RuntimeException runtimeException) {
        logger.error("运行时异常:", runtimeException);
        return new Result(BusinessCodeMessage.FAIL, i18nUtil.i18n(SystemErrorCode.SYSTEM_ERROR));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception exception) {
        logger.error("异常:", exception);
        return new Result(BusinessCodeMessage.FAIL, i18nUtil.i18n(SystemErrorCode.SYSTEM_ERROR));
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public Result handBindException(Exception e) {
        logger.debug("", e);
        BindingResult bindingResult;
        if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        } else {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<String> messages = new ArrayList<>(allErrors.size());
        for (ObjectError err : allErrors) {
            String message = err.getDefaultMessage();
            if (err instanceof FieldError) {
                FieldError fe = (FieldError) err;
                message = fe.getField() + ":" + message;
            }
            messages.add(message);
        }
        return new Result(BusinessCodeMessage.PARAMETER_FAIL, String.join(",", messages));
    }

    @ExceptionHandler({JsonMappingException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Result handleInvalidParamException(Exception e) {
        return new Result(BusinessCodeMessage.REQUEST_PARAM_ERROR, i18nUtil.i18n(BusinessCodeMessage.REQUEST_PARAM_ERROR));
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result handleNotFound() {
        return new Result(BusinessCodeMessage.REQUEST_NO_HANDLER_FOUND, i18nUtil.i18n(BusinessCodeMessage.REQUEST_NO_HANDLER_FOUND));
    }

}
