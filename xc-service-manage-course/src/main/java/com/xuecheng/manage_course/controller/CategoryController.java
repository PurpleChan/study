package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.controller
 * @version:1.0
 */
@RestController
public class CategoryController implements CategoryControllerApi
{
    @Autowired
    private CategoryService categoryService;

    //查询所有的课程类目
    @Override
    @GetMapping("category/list")
    public CategoryNode selAll()
    {
        return categoryService.selAll();
    }
}
