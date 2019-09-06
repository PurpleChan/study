package com.xuecheng.api.course;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.api.course
 * @version:1.0
 */
public interface SysdictionaryControllerApi
{
    @ApiOperation("查询字典")
    SysDictionary selOne(String dtype);

}
