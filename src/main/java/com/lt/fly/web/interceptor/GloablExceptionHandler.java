package com.lt.fly.web.interceptor;


import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jinbin
 * @date 2018-07-08 22:37
 */
@ControllerAdvice
public class GloablExceptionHandler {
	
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器错误";
        }
        return HttpResult.failure(ResultCode.SERVER_ERROR.getCode(), msg);
    }
    
    @ResponseBody
    @ExceptionHandler(ClientErrorException.class)
    public Object handleClientErrorException(ClientErrorException e) {
    	return HttpResult.failure(ResultCode.CLIENT_ERROR.getCode(), e.getMessage());
    }
}
