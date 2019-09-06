package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/29
 * @Description:com.xuecheng.api.course
 * @version:1.0
 */
public interface CourseControllerApi
{
    @ApiOperation("课程计划管理")
    TeachplanNode teacherPlanList(String courseId);

    @ApiOperation("新增课程计划")
    ResponseResult teachplanAdd(Teachplan teachplan);

    @ApiOperation("分页查询我的课程")
    QueryResponseResult selCourseList(int pageNum, int pageSize, CourseListRequest courseListRequest);

    @ApiOperation("新增coursebase")
    ResponseResult coursebaseAdd(CourseBase courseBase);

    @ApiOperation("保存课程图片")
    ResponseResult coursepicAdd(CoursePic coursePic);

    @ApiOperation("查询课程图片")
    CoursePic coursepicSel(String id);

    @ApiOperation("删除课程图片")
    ResponseResult coursepicDel(String id);

    @ApiOperation("课程数据")
    CourseView coursePreview(String courseId);

    @ApiOperation("课程预览")
    CoursePublishResult preview(String courseId);

    @ApiOperation("课程发布")
    CoursePublishResult publish(String courseId);

    @ApiOperation("关联媒资文件")
    ResponseResult selectMedia(TeachplanMedia teachplanMedia);


}
