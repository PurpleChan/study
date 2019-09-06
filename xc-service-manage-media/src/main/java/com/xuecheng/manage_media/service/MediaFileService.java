package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/16
 * @Description:com.xuecheng.manage_media.service
 * @version:1.0
 */
@Service
public class MediaFileService
{
    @Autowired
    private MediaFileRepository mediaFileRepository;

    /**
     * 查询所有媒资文件
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    public QueryResponseResult list(int page, int size, QueryMediaFileRequest queryMediaFileRequest)
    {
        if(page<0)
            page=1;
        if(size<0)
            size=9;
        Pageable pageable = PageRequest.of(page-1, size);
        MediaFile mediaFile = new MediaFile();
        mediaFile.setTag(queryMediaFileRequest.getTag()!=null&&!queryMediaFileRequest.getTag().equals("")?queryMediaFileRequest.getTag():null);
        mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName()!=null&&!queryMediaFileRequest.getFileOriginalName().equals("")?queryMediaFileRequest.getFileOriginalName():null);
        mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus()!=null&&!queryMediaFileRequest.getProcessStatus().equals("")?queryMediaFileRequest.getProcessStatus():null);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING, true));
        //ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MediaFile> example = Example.of(mediaFile,exampleMatcher);
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        List<MediaFile> mediaFiles = all.getContent();
        QueryResult<MediaFile> queryResult=new QueryResult<>();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(mediaFiles);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }


}
