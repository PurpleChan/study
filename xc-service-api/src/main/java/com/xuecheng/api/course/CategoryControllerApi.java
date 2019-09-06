package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.api.course
 * @version:1.0
 */
public interface CategoryControllerApi
{
    @ApiOperation("查询所有的课程类目")
    CategoryNode selAll();
}
