package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms.controller
 * @version:1.0
 */
@Controller
public class CmsPagePreviewController extends BaseController
{
    @Autowired
    private PageService pageService;

    @RequestMapping(value = "/cms/preview/{pageId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String cmsPreview(@PathVariable("pageId") String id)
    {
        String html = pageService.cmsPreview(id);
        return html;
    }
}
