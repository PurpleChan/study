package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.manage_course.dao
 * @version:1.0
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia,String >
{
    List<TeachplanMedia> findByCourseId(String courseId);
}
