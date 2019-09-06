package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.dao
 * @version:1.0
 */
public interface SysdictionaryRepository extends MongoRepository<SysDictionary, String>
{
    public SysDictionary findByDType(String dType);
}
