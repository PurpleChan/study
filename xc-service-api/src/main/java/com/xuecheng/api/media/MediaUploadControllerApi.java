package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/15
 * @Description:com.xuecheng.api.media
 * @version:1.0
 */
public interface MediaUploadControllerApi
{
    @ApiOperation("文件上传注册")
    ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    @ApiOperation("分块检查")
    ResponseResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize);

    @ApiOperation("上传分块")
    ResponseResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5);

    @ApiOperation("合并分块")
    ResponseResult mergeChunk(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);


}
