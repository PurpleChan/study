package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/19
 * @Description:com.xuecheng.manage_cms.controller
 * @version:1.0
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String>
{

}
