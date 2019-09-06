package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.ext.GetMediaResult;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.api.learning
 * @version:1.0
 */
public interface CourseLearningControllerApi
{
    @ApiOperation("获取课程学习地址")
    GetMediaResult getMedia(String courseId, String teachplanId);
}
