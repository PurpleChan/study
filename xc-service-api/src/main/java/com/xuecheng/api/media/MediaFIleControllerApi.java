package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/16
 * @Description:com.xuecheng.api.media
 * @version:1.0
 */
public interface MediaFIleControllerApi
{
    @ApiOperation("查询所有媒资文件")
    QueryResponseResult list(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

}
