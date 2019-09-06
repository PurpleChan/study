package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/29
 * @Description:com.xuecheng.manage_course.dao
 * @version:1.0
 */
@Mapper
@Component
public interface TeachplanMapper
{
    List<TeachplanNode> selAll(String courseId);
}
