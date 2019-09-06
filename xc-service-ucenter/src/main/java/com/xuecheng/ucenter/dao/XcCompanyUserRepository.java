package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.ucenter.dao
 * @version:1.0
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String>
{
    XcCompanyUser findByUserId(String userId);
}
