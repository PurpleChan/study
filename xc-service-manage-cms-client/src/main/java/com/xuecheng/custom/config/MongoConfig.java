package com.xuecheng.custom.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom.config
 * @version:1.0
 */
@Configuration
public class MongoConfig 
{
    @Value("${spring.data.mongodb.database}")
    private String dataBase;

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient)
    {
        MongoDatabase database = mongoClient.getDatabase(dataBase);
        return  GridFSBuckets.create(database);
    }
}
