package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.manage_cms.dao
 * @version:1.0
 */

public interface CmsPageRepository extends MongoRepository<CmsPage,String>
{
    //根据页面名称查询
    List<CmsPage> findAllByPageAliase(String pageAliase);

    //根据页面名称和类型查询
    List<CmsPage> findAllByPageNameAndPageType(String pageName, String pageType);

    //根据站点站点和页面类型查询记录数
    int countAllBySiteIdAndPageType(String siteId, String pageType);

    //根据站点和页面类型分页查询
    Page<CmsPage> findBySiteIdAndPageType(String siteId, String pageType, Pageable pageable);

    //查询页面是否存在
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);

}
