package com.xuecheng.custom.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.custom.config.RabbitConfig;
import com.xuecheng.custom.service.PostPageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom.mq
 * @version:1.0
 */
@Component
public class CmsConsumer
{
    @Autowired
    private PostPageService postPageService;

    @RabbitListener(queues = {RabbitConfig.post_queue})
    public void postPage(String message)
    {
        Map map = JSON.parseObject(message, Map.class);
        postPageService.postPage((String)map.get("pageId"));

    }
}
