package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/20
 * @Description:com.xuecheng.govern.gateway.service
 * @version:1.0
 */
@Service
public class GatewayService
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 校验cookie中的令牌
     * @param request
     * @return
     */
    public boolean checkTokenFromCookie(HttpServletRequest request)
    {
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if(map==null)
            return false;
        String access_token = map.get("uid");
        if(access_token==null)
            return false;
        Long expire = stringRedisTemplate.getExpire("user_token:" + access_token);
        return expire>0;
    }

    /**
     *校验请求头中的令牌
     * @param request
     * @return
     */
    public boolean checkJwtFromHeader(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        if(authorization==null)
            return false;
        if(authorization.startsWith("Bearer "))
            return false;
        return true;
    }
}
