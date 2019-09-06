package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.response.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.ext.GetMediaResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.learning.service
 * @version:1.0
 */
@Service
public class CourseLearningService
{

    @Autowired
    private CourseSearchClient courseSearchClient;

    /**
     * 获取课程学习地址
     * @param courseId
     * @param teachplanId
     * @return
     */
    public GetMediaResult getMedia(String courseId, String teachplanId)
    {
        //校验学生的学习权限  是否自费等等
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getTeachplanMediaPub(teachplanId);
        if(teachplanMediaPub!=null)
        {
            String mediaUrl = teachplanMediaPub.getMediaUrl();
            if (mediaUrl != null && !mediaUrl.equals(""))
            {
                return new GetMediaResult(CommonCode.SUCCESS, mediaUrl);
            }
        }
        return new GetMediaResult(CommonCode.FAIL, null);
    }
}
