package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.dao.SysdictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/31
 * @Description:com.xuecheng.manage_course.service
 * @version:1.0
 */
@Service
public class SysdictionaryService
{
    @Autowired
    private SysdictionaryRepository sysdictionaryRepository;

    /**
     * 查询字典
     * @param dType
     * @return
     */
    public SysDictionary selOne(String dType)
    {
        SysDictionary sysDictionary = sysdictionaryRepository.findByDType(dType);
        return sysDictionary;
    }
}
