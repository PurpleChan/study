package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.dao
 * @version:1.0
 */
@Mapper
@Component
public interface CategoryMapper
{
    public CategoryNode selAll();
}
