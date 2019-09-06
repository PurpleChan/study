package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms.dao
 * @version:1.0
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String>
{

}
