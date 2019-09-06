package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.manage_cms
 * @version:1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest
{
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindPage()
    {
//        Pageable pageable = new PageRequest(1, 3);
//        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
//        System.out.println(all);


        List<CmsPage> cmsPages = cmsPageRepository.findAllByPageAliase("课程详情页面");
        List<CmsPage> allByPageNameAndPageType = cmsPageRepository.findAllByPageNameAndPageType("preview_123.html", "1");
        int i = cmsPageRepository.countAllBySiteIdAndPageType("5a751fab6abb5044e0d19ea1", "0");
        Pageable pageable=PageRequest.of(0,5);
        Page<CmsPage> bySiteIdAndPageType = cmsPageRepository.findBySiteIdAndPageType("5a751fab6abb5044e0d19ea1", "0", pageable);
        System.out.println(i);

    }
}
