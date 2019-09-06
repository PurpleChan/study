package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms
 * @version:1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGridFS
{
    @Autowired
    private Configuration configuration;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test//mongobd查文件  下文件
    public void testguidfs() throws IOException
    {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5d33199aca6ff64b7c4cf478")));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

        String html = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(html);
    }

    //上传给mongodb文件
    @Test
    public void testaddgrid() throws FileNotFoundException
    {
        File file = new File("D:/01.项目/3.学成在线项目实战/day09 课程预览 Eureka Feign/资料/课程详情页面模板/course.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "课程模板文件", "");
        System.out.println(file);
        System.out.println(objectId);

    }

}
