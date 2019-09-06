package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaFIleControllerApi;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/16
 * @Description:com.xuecheng.manage_media.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFIleControllerApi
{
    @Autowired
    private MediaFileService mediaFileService;
    //查询所有媒资文件
    @Override
    @GetMapping("/list/{page}/{size}/")
    public QueryResponseResult list(@PathVariable("page") int page,@PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest)
    {
        return mediaFileService.list(page,size,queryMediaFileRequest);
    }
}
