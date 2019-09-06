package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/19
 * @Description:com.xuecheng.ucenter.dao
 * @version:1.0
 */
public interface XcUserRepository extends JpaRepository<XcUser,String>
{
    XcUser findByUsername(String username);
}
