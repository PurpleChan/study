package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms.controller
 * @version:1.0
 */
@RestController
public class CmsConfigController implements CmsConfigControllerApi
{
    @Autowired
    private CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/cms/config/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id)
    {
       return cmsConfigService.getModel(id);
    }
}
