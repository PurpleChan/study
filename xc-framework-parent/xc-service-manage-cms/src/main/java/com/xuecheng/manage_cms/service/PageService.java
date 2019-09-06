package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.config.RabbitConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/16
 * @Description:com.xuecheng.manage_cms.service
 * @version:1.0
 */
@Service
public class PageService
{
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * cmspage表的分页查询
     * @param page 当前页码  从1开始
     * @param size 查询条数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest)
    {
        CmsPage cmsPage=new CmsPage();
        if(page<0)
            page=0;
        if(size<=0)
            size=10;
        cmsPage.setPageAliase((queryPageRequest.getPageAliase()!=null&&!queryPageRequest.getPageAliase().equals(""))?queryPageRequest.getPageAliase():null);
        cmsPage.setPageId((queryPageRequest.getPageId()!=null&&!queryPageRequest.getPageId().equals(""))?queryPageRequest.getPageId():null);
        cmsPage.setPageName((queryPageRequest.getPageName()!=null&&(!queryPageRequest.getPageName().equals("")))?queryPageRequest.getPageName():null);
        cmsPage.setSiteId((queryPageRequest.getSiteId()!=null&&!queryPageRequest.getSiteId().equals(""))?queryPageRequest.getSiteId():null);
        cmsPage.setTemplateId((queryPageRequest.getTemplateId()!=null&&!queryPageRequest.getTemplateId().equals(""))?queryPageRequest.getTemplateId():null);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        Pageable pageable = PageRequest.of(page,size);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        QueryResult<CmsPage> queryResult=new QueryResult<CmsPage>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 查询所有的站点id和名称
     * @return
     */
    public QueryResponseResult findSiteList()
    {
        List<CmsSite> list = cmsSiteRepository.findAll();
        QueryResult<CmsSite> queryResult=new QueryResult<>();
        queryResult.setList(list);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 新增/修改页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult cmsPageAdd(CmsPage cmsPage)
    {
        try
        {
            cmsPage = cmsPageRepository.save(cmsPage);
        }
        catch (Exception e)
        {
//            e.printStackTrace();
//            return new CmsPageResult(CommonCode.FAIL,cmsPage);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }


    /**
     * 查询cms template 模板列表
     *
     * @return
     */
    public QueryResponseResult cmsTemplateList()
    {
        QueryResult<CmsTemplate> queryResult=new QueryResult<>();
        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        queryResult.setList(all);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 删除cmspage
     * @param pageId
     * @return
     */
    public ResultCode cmsPageDelete(String pageId)
    {
        cmsPageRepository.deleteById(pageId);
        return CommonCode.SUCCESS;
    }


    /**
     * 页面静态化
     * @param id
     * @return
     */
    public String cmsPreview(String id)
    {
        //根据页面的id获取cmspage
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        CmsPage cmsPage = optional.get();

        //获取cmspage的dataurl属性
        String dataUrl = cmsPage.getDataUrl();
        if(dataUrl==null||dataUrl.equals(""))
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);

        //根据dataurl远程请求cmsconfig的响应的cmsconfig  并获取其model属性
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        //List model = (ArrayList)(entity.getBody().get("model"));
        Map model = entity.getBody();
        if(model==null)
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);

        //获取cmspage的templateId属性
        String templateId=cmsPage.getTemplateId();
        if(templateId==null||templateId.equals(""))
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);

        //根据模板id获取模板
        Optional<CmsTemplate> optional1 = cmsTemplateRepository.findById(templateId);
        if(!optional1.isPresent())
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        CmsTemplate cmsTemplate = optional1.get();

        //根据模板的fileid获取模板文件内容
        String templateFileId = cmsTemplate.getTemplateFileId();
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));


        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

        //创建gridfsresource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);

        //下载模板we文件的内容
        String content=null;
        try
        {
            content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(content==null||content.equals(""))
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);

        //生成模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",content);

        //生成配置类
        Configuration configuration = new Configuration(Configuration.getVersion());

        //配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);

        String html=null;
        try
        {
            //获取模板
            Template template = configuration.getTemplate("template");

            //把模板和数据模型生成页面静态化
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(html==null||html.equals(""))
            ExceptionCast.cast(CmsCode.CMS_COURSE_PERVIEWISNULL);
        return html;
    }

    /**
     * 页面发布
     * @return
     */
    public ResponseResult postPage(String pageId)
    {
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        if(!optionalCmsPage.isPresent())
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        CmsPage page = optionalCmsPage.get();
        String htmlFileId = page.getHtmlFileId();
        if(htmlFileId!=null)
        {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        String content = cmsPreview(pageId);
        ObjectId objectId=null;
        try
        {
            objectId= gridFsTemplate.store(IOUtils.toInputStream(content, "utf-8"), page.getPageAliase());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        page.setHtmlFileId(objectId.toString());
        cmsPageRepository.save(page);
        Map<String,Object> map=new HashMap<>();
        map.put("pageId",pageId);
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        String siteId = page.getSiteId();
        String message = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitConfig.post_exchange,siteId,message);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage)
    {
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cmsPage1!=null)
        {
            cmsPage.setPageId(cmsPage1.getPageId());
        }
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,save);
    }

    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage)
    {
        CmsPageResult cmsPageResult = save(cmsPage);
        if (!cmsPageResult.isSuccess())
            return new CmsPostPageResult(CommonCode.FAIL, null);

        CmsPage newCmsPage = cmsPageResult.getCmsPage();
        String pageId = cmsPage.getPageId();

        ResponseResult responseResult = postPage(pageId);

        if(!responseResult.isSuccess())
            return new CmsPostPageResult(CommonCode.FAIL, null);

        String siteId = newCmsPage.getSiteId();
        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);
        if(!cmsSiteOptional.isPresent())
            return new CmsPostPageResult(CommonCode.FAIL, null);

        CmsSite cmsSite = cmsSiteOptional.get();
        String siteDomain = cmsSite.getSiteDomain();
        String siteWebPath = cmsSite.getSiteWebPath();
        String pageWebPath = newCmsPage.getPageWebPath();
        String pageName = newCmsPage.getPageName();

        String pageUrl=siteDomain+siteWebPath+pageWebPath+pageName;
        return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
    }
}

