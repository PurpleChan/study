package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.manage_cms
 * @version:1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.xuecheng.framework.domain.cms") //扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"}) //扫描接口包
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"}) //扫描本项目下的所有包
@ComponentScan(basePackages = {"com.xuecheng.framework.exception"})
public class ManageCmsApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManageCmsApplication.class, args);
    }

    //配置resttemplate到spring的容器中    用于发送http的请求
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
