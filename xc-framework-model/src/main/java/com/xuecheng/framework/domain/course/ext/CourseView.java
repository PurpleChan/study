package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/10
 * @Description:com.xuecheng.framework.domain.course.ext
 * @version:1.0
 */
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable
{
    private CourseBase courseBase;
    private CourseMarket courseMarket;
    private CoursePic coursePic;
    private TeachplanNode teachplanNode;
}
