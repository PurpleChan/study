package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.auth.controller
 * @version:1.0
 */
@RestController
public class AuthController implements AuthControllerApi
{
    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    private AuthService authService;

    //登录
    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest)
    {
        //校验账号是否输入
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }

        //校验密码是否输入
        if(StringUtils.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }

        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
        String access_token = authToken.getAccess_token();
        saveCookie(access_token);

        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    //把令牌保存到cookie中
    private void saveCookie(String token)
    {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    //退出
    @Override
    public ResponseResult logut()
    {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String access_token = getTokenFromCookie();
        ResponseResult responseResult=authService.logout(access_token);
        CookieUtil.addCookie(response,cookieDomain,"/","uid","",0,false);
        return authService.logout(access_token);
    }

    //查询jwt令牌
    @Override
    @GetMapping("/userjwt")
    public JwtResult getJwt()
    {
        String access_token = this.getTokenFromCookie();
        if(access_token==null)
            return new JwtResult(CommonCode.FAIL, null);

        AuthToken authToken=authService.getJwt(access_token);
        if(authToken==null)
            return new JwtResult(CommonCode.FAIL, null);
        return new JwtResult(CommonCode.SUCCESS, authToken.getJwt_token());

    }

    //从cookie中读取访问令牌
    private String getTokenFromCookie()
    {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String access_token = map.get("uid");
        return access_token;
    }
}
