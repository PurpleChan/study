package com.xuecheng.custom.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/22
 * @Description:com.xuecheng.custom.dao
 * @version:1.0
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String>
{

}
