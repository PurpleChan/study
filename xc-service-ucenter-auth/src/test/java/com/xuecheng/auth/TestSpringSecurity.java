package com.xuecheng.auth;

import org.bson.internal.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.auth
 * @version:1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringSecurity
{
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;


    @Test
    public void testclient()
    {
        //采用客户端负债均衡  从eureka获取认证服务的端口和ip
        ServiceInstance serviceInstance = loadBalancerClient.choose("xc-service-ucenter-auth");

        //获取申请令牌的url
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        //请求内容包含两个部分
        //1.头信息  需要包含http basic的信息
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic=httpbasic("xcWebApp", "xcWebApp");
        headers.add("Authorization", httpbasic);

        //2.body包含 grant_type  username   password
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username","itcast");
        body.add("password","123");

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
        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST,multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }


    //将客户端id和客户端密码拼接  并进行64位编码
    private String httpbasic(String clientId, String clientSecret)
    {
        String string = clientId + clientSecret;
        String encode = Base64.encode(string.getBytes());
        return "Basic "+encode;
    }
}
