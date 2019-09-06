package com.xuecheng.custom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom
 * @version:1.0
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms") //扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.framework.exception"})
@ComponentScan(basePackages = {"com.xuecheng.custom"})
public class CmsClientApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CmsClientApplication.class, args);
    }
}
