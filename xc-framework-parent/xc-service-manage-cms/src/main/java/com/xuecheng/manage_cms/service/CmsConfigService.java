package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/20
 * @Description:com.xuecheng.manage_cms.service
 * @version:1.0
 */
@Service
public class CmsConfigService
{
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    /**
     * 根据id获取cmsconfig
     * @param id
     * @return
     */
    public CmsConfig getModel(String id)
    {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(!optional.isPresent())
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        return optional.get();
    }
}
