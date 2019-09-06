package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.auth.service
 * @version:1.0
 */
@Service
public class AuthService
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //登录
    public AuthToken login(String username,String password,String clientId,String clienSecret)
    {
        AuthToken authToken = applyToken(username, password, clientId, clienSecret);
        if(authToken==null)
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);

        //将token存到redis
        boolean saveToken = saveToken(authToken);
        if(!saveToken)
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        return authToken;
    }


    //申请令牌
    public AuthToken applyToken(String username,String password,String clientId,String clientSecret)
    {
        //采用客户端负债均衡  从eureka获取认证服务的端口和ip
        ServiceInstance serviceInstance = loadBalancerClient.choose("xc-service-ucenter-auth");

        //获取申请令牌的url
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        //请求内容包含两个部分
        //1.头信息  需要包含http basic的信息
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic=httpbasic(clientId, clientSecret);
        headers.add("Authorization", httpbasic);

        //2.body包含 grant_type  username   password
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body, headers);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException
            {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        Map resultBody=null;
        try
        {
            //由于restTemplate收到400或401的错误会抛出异常，而spring security针对账号不存在及密码错误会返回400及
            //401，所以在代码中控制针对400或401的响应不要抛出异常。
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                @Override
                public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException
                {
                    //设置当响应400和401的时候照常响应数据  不要报错
                    if(response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401)
                        super.handleError(response);
                }
            });

            //远程调用申请令牌
            ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST,multiValueMapHttpEntity, Map.class);
            resultBody = exchange.getBody();
            //System.out.println(body1);
        }
        catch (RestClientException e)
        {
            e.printStackTrace();
            LOG.error("request oauth_token_password error: {}",e.getMessage());
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        //设置异常对应的类型
        if(resultBody==null||resultBody.get("access_token")==null||resultBody.get("refresh_token")==null||resultBody.get("jti")==null)
        {
            String error_description = (String) resultBody.get("erreo_description");
            if(error_description.equals("坏的凭证"))
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            else if(error_description.equals("UserDetailsService returned null"))
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
        }

        String access_token = (String) resultBody.get("access_token");
        String refresh_token = (String) resultBody.get("refresh_token");
        String jti = (String) resultBody.get("jti");

        AuthToken authToken = new AuthToken();
        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        authToken.setJwt_token(jti);

        return authToken;
    }


    //将客户端id和客户端密码拼接  并进行64位编码
    private String httpbasic(String clientId, String clientSecret)
    {
        String string = clientId+":" + clientSecret;
        String encode = Base64.encode(string.getBytes());
        return "Basic "+encode;
    }

    //将authtoken存到redis
    private boolean saveToken(AuthToken authToken)
    {
        String access_token = authToken.getAccess_token();
        String key="user_token:"+access_token;
        String content = JSON.toJSONString(authToken);
        stringRedisTemplate.boundValueOps(key).set(content,tokenValiditySeconds, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key);
        return expire>0;
    }

    /**
     * 获取jwt令牌
     * @param access_token
     * @return
     */
    public AuthToken getJwt(String access_token)
    {
        String userToken="user_token:"+access_token;
        String result = stringRedisTemplate.opsForValue().get(userToken);
        if(result!=null)
        {
            AuthToken authToken = JSON.parseObject(result, AuthToken.class);
            return authToken;
        }
        return null;

    }

    /**退出
     * 从redis中删除令牌
     * @param access_token
     * @return
     */
    public ResponseResult logout(String access_token)
    {
        String key = "user_token:" + access_token;
        stringRedisTemplate.delete(key);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
