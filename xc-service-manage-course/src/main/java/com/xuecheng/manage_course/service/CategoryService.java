package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.service
 * @version:1.0
 */
@Service
public class CategoryService
{
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 查询所有的课程类目
     * @return
     */
    public CategoryNode selAll()
    {
        CategoryNode categoryNode = categoryMapper.selAll();
        return categoryNode;
    }

}
