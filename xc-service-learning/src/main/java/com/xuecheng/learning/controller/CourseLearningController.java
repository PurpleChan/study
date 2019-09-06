package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.ext.GetMediaResult;
import com.xuecheng.learning.service.CourseLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.learning.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi
{

    @Autowired
    private CourseLearningService courseLearningService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getMedia(String courseId, String teachplanId)
    {
        return courseLearningService.getMedia(courseId,teachplanId);
    }
}
