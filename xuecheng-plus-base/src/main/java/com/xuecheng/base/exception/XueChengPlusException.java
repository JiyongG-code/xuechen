package com.xuecheng.base.exception;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/17 14:33
 */
public class XueChengPlusException extends RuntimeException{

    private String errMessage;

    public XueChengPlusException() {
        super();
    }
    public String getErrMessage(){
        return  errMessage;
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage=message;
    }

    public static void cast(String errMessage){
        throw  new  XueChengPlusException(errMessage);
    }
    public static void cast(CommonError commonError){
        throw  new  XueChengPlusException( commonError.getErrMessage());
    }
}
