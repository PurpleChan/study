package com.xuecheng.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/21
 * @Description:com.xuecheng.manage_cms
 * @version:1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test_rabbit_provider
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void provider()
    {
//        rabbitTemplate.convertAndSend(RabbitConfig.exchange_topic,"inform.email","我是邮件!!!");
//        rabbitTemplate.convertAndSend(RabbitConfig.exchange_topic,"inform.sms","我是短信!!!");
    }

}
