package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.api.cms
 * @version:1.0
 */
@Api(value = "Cms页面管理接口")
public interface CmsPageControllerApi
{
    @ApiOperation(value = "分页查询页面列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page",value = "页",required = true,paramType = "path",dataType = "int"),
//            @ApiImplicitParam(name = "size",value = "每页记录",required = true,paramType = "path",dataType = "int")
//
//    })
    QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation(value = "查询所有的站点列表")
    QueryResponseResult findSiteIdList();

    @ApiOperation("新增/修改cmspage页面")
    CmsPageResult cmsPageAdd( CmsPage cmsPage);

    @ApiOperation("查询cmstemplate模板")
    QueryResponseResult findCmsTemplateList();

    @ApiOperation(("删除cmspage页面"))
    ResultCode cmspagedelete(String siteId);

    @ApiOperation("页面发布")
    ResponseResult postPage(String pageId);

    @ApiOperation("保存页面")
    CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布页面")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
