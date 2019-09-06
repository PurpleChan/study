package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.SysdictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.SysdictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.controller
 * @version:1.0
 */
@RestController
public class SysdictionaryController implements SysdictionaryControllerApi
{
    @Autowired
    private SysdictionaryService sysdictionaryService;

    @Override
    @GetMapping("sys/dictionary/get/{dType}")
    public SysDictionary selOne(@PathVariable("dType") String dType)
    {
        return  sysdictionaryService.selOne(dType);
    }
}
