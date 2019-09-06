package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.manage_cms.controller
 * @version:1.0
 */
@RestController
public class CmsPageController implements CmsPageControllerApi
{
    @Autowired
    private PageService pageService;


    @Override
    @GetMapping("cms/pagelist")
    public QueryResponseResult findPageList(@RequestParam("page") int page,@RequestParam("size") int size, QueryPageRequest queryPageRequest)
    {
        return pageService.findPageList(page-1,size,queryPageRequest);
    }

    @GetMapping("cms/sitelist")
    @Override
    public QueryResponseResult findSiteIdList()
    {
        return pageService.findSiteList();
    }

    @Override
    @PostMapping("/cms/pageadd")
    public CmsPageResult cmsPageAdd(@RequestBody CmsPage cmsPage)
    {
        return pageService.cmsPageAdd(cmsPage);
    }

    @GetMapping("/cms/templatelist")
    @Override
    public QueryResponseResult findCmsTemplateList()
    {
        return pageService.cmsTemplateList();
    }

    @Override
    @GetMapping("/cms/pagedelete")
    public ResultCode cmspagedelete(@RequestParam("pageId") String pageId)
    {
        return pageService.cmsPageDelete(pageId);
    }

    @Override
    @GetMapping("/cms/pagepost/{pageId}")
    public ResponseResult postPage(@PathVariable("pageId") String pageId)
    {
        return pageService.postPage(pageId);
    }

    @Override
    @PostMapping("/cms/pagesave")
    public CmsPageResult save(@RequestBody CmsPage cmsPage)
    {
        return pageService.save(cmsPage);
    }

    @Override
    @PostMapping("/cms/postpagequick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage)
    {
        return pageService.postPageQuick(cmsPage);
    }
}
