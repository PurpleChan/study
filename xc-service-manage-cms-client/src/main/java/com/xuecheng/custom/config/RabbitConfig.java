package com.xuecheng.custom.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom.config
 * @version:1.0
 */
@Configuration
public class RabbitConfig
{

    public static final String post_exchange="post_exchange";
    public static final  String post_queue="queue_cms_postpage1";
    public static final String post_queue_routingkey="5a751fab6abb5044e0d19ea1";

    //配置交换机
    @Bean(post_exchange)
    public Exchange post_exchange()
    {
        return ExchangeBuilder.directExchange(post_exchange).durable(true).build();
    }

    //配置队列
    @Bean(post_queue)
    public Queue post_queue()
    {
        return new Queue(post_queue);
    }

    //绑定队列到交换机
    @Bean
    public Binding exchange_post_queue(@Qualifier(post_queue) Queue queue,@Qualifier(post_exchange) Exchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(post_queue_routingkey).noargs();
    }

}
