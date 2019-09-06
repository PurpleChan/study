package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.api.cms
 * @version:1.0
 */
public interface CmsConfigControllerApi
{
    /**
     * 通过cmsconfig的id获取cmsconfig
     * @param id
     * @return
     */
    @ApiOperation("通过id获取cmsconfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value="Id",required = true,dataType = "String",paramType = "path")
    })
    CmsConfig getModel(String id);
}
