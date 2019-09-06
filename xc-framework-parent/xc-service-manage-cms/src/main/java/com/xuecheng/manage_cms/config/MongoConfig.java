package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms.config
 * @version:1.0
 */

//配置mongo的配置类  放入spring的容器中
@Configuration
public class MongoConfig
{
    @Value("${spring.data.mongodb.database}")
    String dataBase;

    //用于打开下载流对象
    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient)
    {
        MongoDatabase database = mongoClient.getDatabase(dataBase);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }


}
