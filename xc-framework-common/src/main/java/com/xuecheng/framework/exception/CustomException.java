package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.framework.exception
 * @version:1.0
 */

public class CustomException extends RuntimeException
{
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode)
    {
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode()
    {
        return this.resultCode;
    }
}
