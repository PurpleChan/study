package com.xuecheng.custom.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.custom.dao.CmsPageRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom.service
 * @version:1.0
 */
@Service
public class PostPageService
{
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 把静态化页面下载到前端磁盘
     * @param pageId
     */
    public void postPage(String pageId)
    {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(!optional.isPresent())
            return ;
        CmsPage cmsPage = optional.get();
        String htmlFileId = cmsPage.getHtmlFileId();
        String path = cmsPage.getPagePhysicalPath()+ cmsPage.getPageName();

        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        ObjectId objectId = gridFSFile.getObjectId();
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(objectId);
        GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);

        InputStream inputStream=null;
        FileOutputStream fileOutputStream=null;
        try
        {
            inputStream = gridFsResource.getInputStream();
            fileOutputStream=new FileOutputStream(path);
            IOUtils.copy(inputStream, fileOutputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
