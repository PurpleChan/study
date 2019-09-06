package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.framework.exception
 * @version:1.0
 */
@ControllerAdvice
public class ExceptionCatch
{
    private static final Logger log= LoggerFactory.getLogger(ExceptionCatch.class);

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e)
    {
        log.error("***异常***:{}\r\nexception:",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult=new ResponseResult(resultCode);
        return responseResult;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e)
    {
        log.error("***异常***:{}\r\nexception:",e.getMessage(),e);
        ResponseResult responseResult=new ResponseResult(CommonCode.SERVER_ERROR);
        return responseResult;
    }
}
