package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.api.ucenter
 * @version:1.0
 */
public interface UserControllerApi
{
    @ApiOperation("根据用户名查询用户扩展信息")
    XcUserExt getUserExt(String username);
}
