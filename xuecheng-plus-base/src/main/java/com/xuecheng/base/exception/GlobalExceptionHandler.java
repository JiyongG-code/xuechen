package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/17 14:45
 */
@Slf4j
@ControllerAdvice//控制器增强
public class GlobalExceptionHandler {



    //处理XueChengPlusException，此异常可预知的异常
    @ResponseBody//将信息返回为json格式
    @ExceptionHandler(XueChengPlusException.class)//此方法捕获XueChengPlusException异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//状态码返回500
    public RestErrorResponse  doXueChengPlusException(XueChengPlusException e){
        log.error("捕获异常:{}",e.getErrMessage());
        e.printStackTrace();

        String errMessage = e.getErrMessage();
        return  new RestErrorResponse(errMessage);

    }


    //捕获不可预知异常Exception
    @ResponseBody//将信息返回为json格式
    @ExceptionHandler(Exception.class)//此方法捕获Exception异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//状态码返回500
    public RestErrorResponse  doException(Exception e){
        log.error("捕获异常:{}",e.getMessage());
        e.printStackTrace();

        String errMessage =e.getMessage();
        return  new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

    }

    @ResponseBody//将信息返回为json格式
    @ExceptionHandler(MethodArgumentNotValidException.class)//此方法捕获Exception异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//状态码返回500
    public RestErrorResponse  doMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        //校验的错误消息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer errors = new StringBuffer();
        fieldErrors.forEach(error->{
            errors.append(error.getDefaultMessage()).append(",");
        });
        return new RestErrorResponse(errors.toString());

    }
}
