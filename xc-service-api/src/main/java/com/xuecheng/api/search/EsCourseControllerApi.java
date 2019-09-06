package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.response.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/14
 * @Description:com.xuecheng.api.search
 * @version:1.0
 */
public interface EsCourseControllerApi
{
    @ApiOperation("课程搜索")
    QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);

    @ApiOperation("根据课程id查询课程信息")
    Map<String, CoursePub> getall(String id);

    @ApiOperation("根据课程计划查询媒资信息")
    TeachplanMediaPub getMedia(String teachplanId);
}
