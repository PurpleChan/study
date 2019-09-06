package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/4
 * @Description:com.xuecheng.manage_course.client
 * @version:1.0
 */
@FeignClient("XC-SERVICE-MANAGE-CMS")
public interface CmsPageClient
{
    @PostMapping("/cms/pagesave")
    public CmsPageResult save(@RequestBody CmsPage cmsPage);

    @PostMapping("/cms/postpagequick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
