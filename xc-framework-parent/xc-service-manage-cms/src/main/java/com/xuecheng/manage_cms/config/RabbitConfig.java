package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/21
 * @Description:com.xuecheng.manage_cms.config
 * @version:1.0
 */
@Configuration
public class RabbitConfig
{
//    //声明  交换机  队列   队列路由   的名称
//    public static final String exchange_topic="exchange_topic";
//    public static final String queue_sms = "queue_sms";
//    public static final String queue_email = "queue_mail";
//    public static final String queue_sms_routing = "inform.#.sms.#";
//    public static final String queue_email_routing = "inform.#.email.#";
//
//    //配置交换机
//    @Bean(exchange_topic)
//    public Exchange exchange_topic()
//    {
//        return ExchangeBuilder.topicExchange(exchange_topic).durable(true).build();
//    }
//
//    //配置队列
//    @Bean(queue_sms)
//    public Queue queue_sms()
//    {
//        return new Queue(queue_sms);
//    }
//
//    //配置队列
//    public Queue queue_email()
//    {
//        return new Queue(queue_email);
//    }
//
//    //绑定队列
//    public Binding binding_exchange_topic_queue_sms(@Qualifier(exchange_topic) Exchange exchange,@Qualifier(queue_sms) Queue queue)
//    {
//        return BindingBuilder.bind(queue).to(exchange).with(queue_sms_routing).noargs();
//    }
//
//    //绑定队列
//    public Binding bingding_exchange_topic_queue_email(@Qualifier(exchange_topic) Exchange exchange,@Qualifier(queue_email) Queue queue)
//    {
//        return BindingBuilder.bind(queue).to(exchange).with(queue_email_routing).noargs();
//    }

      public static final String post_exchange="post_exchange";

      @Bean(post_exchange)
      public Exchange exchange()
      {
         return  ExchangeBuilder.directExchange(post_exchange).durable(true).build();
      }

}
