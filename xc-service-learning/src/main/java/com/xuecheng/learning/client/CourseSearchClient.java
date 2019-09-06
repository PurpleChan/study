package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.course.response.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.learning.client
 * @version:1.0
 */
@FeignClient("XC-SEARCH-SERVICE")
public interface CourseSearchClient
{
    @GetMapping("/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPub getTeachplanMediaPub(@PathVariable("teachplanId") String teachplanId);
}
