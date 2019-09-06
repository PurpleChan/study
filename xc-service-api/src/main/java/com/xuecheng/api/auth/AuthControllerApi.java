package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.api.auth
 * @version:1.0
 */
public interface AuthControllerApi
{
    @ApiOperation("登录")
    LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    ResponseResult logut();

    @ApiOperation("查询jwt令牌")
    public JwtResult getJwt();
}
