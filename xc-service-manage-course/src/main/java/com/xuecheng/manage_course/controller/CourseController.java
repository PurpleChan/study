package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
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
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/29
 * @Description:com.xuecheng.manage_course.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi
{
    @Autowired
    private CourseService courseService;

    //查询教学计划
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode teacherPlanList(@PathVariable("courseId") String courseId)
    {
        return courseService.selAll(courseId);
    }

    //新增教学计划
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult teachplanAdd(@RequestBody Teachplan teachplan)
    {
        return courseService.teachplanAdd(teachplan);
    }

    //分页查询我的课程列表
    @Override
    @GetMapping("/coursebase/list/{pageNum}/{pageSize}")
    public QueryResponseResult selCourseList(@PathVariable("pageNum") int pageNum,@PathVariable("pageSize") int pageSize, CourseListRequest courseListRequest)
    {
       return  courseService.selCourseList(pageNum, pageSize, courseListRequest);
    }

    //新增coursebase
    @Override
    @PostMapping("coursebase/add")
    public ResponseResult coursebaseAdd(@RequestBody CourseBase courseBase)
    {
        return courseService.coursebaseAdd(courseBase);
    }

    //新增课程图片
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult coursepicAdd(CoursePic coursePic)
    {
       return  courseService.coursepicAdd(coursePic);
    }

    //删除课程图片
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult coursepicDel(@RequestParam("courseId") String id)
    {
       return  courseService.coursepicDel(id);
    }

    //查询课程图片
    @Override
    @GetMapping("/coursepic/list/{id}")
    public CoursePic coursepicSel(@PathVariable("id") String id)
    {
        return   courseService.coursepicSel(id);
    }

    //课程数据
    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseView coursePreview(@PathVariable("courseId") String courseId)
    {
       return courseService.coursePreview(courseId);
    }

    //课程预览
    @Override
    @PostMapping("/preview/{courseId}")
    public CoursePublishResult preview(@PathVariable("courseId") String courseId)
    {
        return courseService.preview(courseId);
    }

    //课程发布
    @Override
    @PostMapping("/publish/{courseId}")
    public CoursePublishResult publish(@PathVariable("courseId") String courseId)
    {
        return courseService.publish(courseId);
    }

    //关联媒资信息
    @Override
    @PostMapping("/savemedia")
    public ResponseResult selectMedia(@RequestBody TeachplanMedia teachplanMedia)
    {
       return courseService.selectMedia(teachplanMedia);
    }
}
