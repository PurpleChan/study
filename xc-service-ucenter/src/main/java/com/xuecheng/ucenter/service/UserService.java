package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.ucenter.service
 * @version:1.0
 */
@Service
public class UserService
{
    @Autowired
    private XcUserRepository xcUserRepository;

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public XcUser findByUsername(String username)
    {
        return xcUserRepository.findByUsername(username);
    }

    /**
     * 根据用户名查询用户扩展信息
     * @param username
     * @return
     */
    public XcUserExt getUserExt(String username)
    {
        XcUser xcUser = this.findByUsername(username);
        if(xcUser==null)
            return null;
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        String userId = xcUser.getId();
        XcCompanyUser companyUser = xcCompanyUserRepository.findByUserId(userId);
        if(companyUser!=null)
        {
            xcUserExt.setCompanyId(companyUser.getCompanyId());
        }
        return xcUserExt;
    }
}
