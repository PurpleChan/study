package com.xuecheng.auth.client;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.auth.client
 * @version:1.0
 */
@FeignClient
public interface UcenterClient
{
    @GetMapping("/ucenter/getuserext")
    public XcUserExt getUserExt(@RequestParam("username") String username);

}
