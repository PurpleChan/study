package com.xuecheng.govern_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/2
 * @Description:PACKAGE_NAME
 * @version:1.0
 */
@EnableEurekaServer   //表示这是一个eureka服务
@SpringBootApplication
public class GovernCenterApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GovernCenterApplication.class, args);
    }
}
