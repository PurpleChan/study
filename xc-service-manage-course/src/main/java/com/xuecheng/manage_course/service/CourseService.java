package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.response.TeachplanMediaPub;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/7/29
 * @Description:com.xuecheng.manage_course.service
 * @version:1.0
 */
@Service
public class CourseService
{

    @Value("${course-publish.datatUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseMapper coursemapper;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CoursePubRepository coursePubRepository;

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;
    /**
     * 查询课程计划
     * @param courseId
     * @return
     */
    public TeachplanNode selAll(String courseId)
    {
        List<TeachplanNode> teachplanNodes = teachplanMapper.selAll(courseId);
        if(teachplanNodes.size()>0)
            return teachplanNodes.get(0);
        return null;
    }

    /**
     * 新增课程计划
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult teachplanAdd(Teachplan teachplan)
    {
        if(teachplan.getParentid()==null||teachplan.getParentid()=="")
        {
            Teachplan teachplanParent = new Teachplan();
            teachplanParent.setCourseid(teachplan.getCourseid());
            teachplanParent.setParentid("0");
            Example<Teachplan> teachplanExample = Example.of(teachplanParent);
            Optional<Teachplan> optional = teachplanRepository.findOne(teachplanExample);
            if(optional.isPresent())
                teachplan.setParentid(optional.get().getId());
            teachplan.setGrade("2");
        }
        else
            teachplan.setGrade("3");
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 分页查询我的课程
     * @param pageNum
     * @param pageSize
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult selCourseList(int pageNum, int pageSize, CourseListRequest courseListRequest)
    {
        PageHelper.startPage(pageNum, pageSize);
        Page<CourseInfo> courseListPage = coursemapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> queryResult=new QueryResult<>();
        queryResult.setTotal(courseListPage.getTotal());
        queryResult.setList(courseListPage.getResult());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 新增coursebase
     * @param courseBase
     * @return
     */
    @Transactional
    public ResponseResult coursebaseAdd(CourseBase courseBase)
    {
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 保存上传的课程图片
     * @param coursePic
     * @return
     */
    @Transactional
    public ResponseResult coursepicAdd(CoursePic coursePic)
    {
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 删除课程图片
     * @param id
     */
    @Transactional
    public ResponseResult coursepicDel(String id)
    {
        coursePicRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询课程的图片
     * @param id
     * @return
     */
    public CoursePic coursepicSel(String id)
    {
        Optional<CoursePic> optional = coursePicRepository.findById(id);
        if(optional.isPresent())
            return optional.get();
        return null;
    }

    /**
     * 课程数据
     * @param courseId
     */
    public CourseView coursePreview(String courseId)
    {
        CourseView courseView=new CourseView();

        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if(courseBaseOptional.isPresent())
            courseView.setCourseBase(courseBaseOptional.get());

        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        if(courseMarketOptional.isPresent())
            courseView.setCourseMarket(courseMarketOptional.get());

        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if(coursePicOptional.isPresent())
            courseView.setCoursePic(coursePicOptional.get());

        List<TeachplanNode> teachplanNodes = teachplanMapper.selAll(courseId);
        if(teachplanNodes!=null&&teachplanNodes.size()>0)
            courseView.setTeachplanNode(teachplanNodes.get(0));

        return courseView;
    }

    /***
     * 课程预览
     * @param courseId
     * @return
     */
    public CoursePublishResult preview(String courseId)
    {
        CmsPage cmsPage=new CmsPage();

        CourseBase courseBase=new CourseBase();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if(courseBaseOptional.isPresent())
            courseBase=courseBaseOptional.get();

        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageName(courseId+".html");
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);

        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);

        if(!cmsPageResult.isSuccess())
            return new CoursePublishResult(null,CommonCode.FAIL);
        String pageId=cmsPageResult.getCmsPage().getPageId();
        String pageUrl=previewUrl+pageId;
        return new CoursePublishResult(pageUrl,CommonCode.SUCCESS);
    }

    /**
     * 课程发布
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String courseId)
    {
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (!courseBaseOptional.isPresent())
        {
            return new CoursePublishResult(null, CommonCode.FAIL);
        }
        CourseBase courseBase = courseBaseOptional.get();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);
        cmsPage.setPageName(courseId+".html");

        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);

        if(!cmsPostPageResult.isSuccess())
            ExceptionCast.cast(CommonCode.SERVER_ERROR);

        //课程缓存

        CoursePub coursePub = createCoursePub(courseId);
        CoursePub newCoursePub = saveCoursePub(courseId, coursePub);
        if(newCoursePub==null)
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);

        String pageUrl = cmsPostPageResult.getPageUrl();

        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);

        //添加课程索引
        saveTeachplanMediaPub(courseId);

        return new CoursePublishResult(pageUrl, CommonCode.SUCCESS);
    }

    //课程发布时调用  把之前的删除 添加到新的表  让logstash扫描存储到es
    private void saveTeachplanMediaPub(String courseId)
    {
        List<TeachplanMedia> list = teachplanMediaRepository.findByCourseId(courseId);
        teachplanMediaRepository.deleteAll(list);
        if(list!=null)
        {
            for(TeachplanMedia teachplanMedia:list)
            {
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
                BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
                teachplanMediaPub.setTimestamp(new Date());
                teachplanMediaPubRepository.save(teachplanMediaPub);
            }
        }
    }


    //创建coursepub对象
    private CoursePub createCoursePub(String id)
    {
        CoursePub coursePub=new CoursePub();
        coursePub.setId(id);

        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent())
        {
            CourseBase courseBase = courseBaseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }

        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if(courseMarketOptional.isPresent())
        {
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }

        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if(coursePicOptional.isPresent())
        {
            CoursePic coursePic = coursePicOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        List<TeachplanNode> teachplanNodes = teachplanMapper.selAll(id);
        if(teachplanNodes!=null)
        {
            TeachplanNode teachplanNode = teachplanNodes.get(0);
            String s = JSON.toJSONString(teachplanNode);
            coursePub.setTeachplan(s);
        }

        return coursePub;
    }

    //保存coursepub
    private CoursePub saveCoursePub(String id,CoursePub coursePub)
    {
        if(StringUtils.isEmpty(id))
            ExceptionCast.cast(CommonCode.FAIL);
        CoursePub newCoursePub = new CoursePub();

        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if(coursePubOptional.isPresent())
        {
            newCoursePub= coursePubOptional.get();
        }

        if(newCoursePub!=null)
        {
            BeanUtils.copyProperties(coursePub,newCoursePub);
        }

        newCoursePub.setId(id);
        newCoursePub.setTimestamp(new Date());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String pubTime = simpleDateFormat.format(new Date());
        newCoursePub.setPubTime(pubTime);

        CoursePub save = coursePubRepository.save(newCoursePub);
        return save;
    }

    /**
     * 关联媒资信息
     * @param teachplanMedia
     * @return
     */
    @Transactional
    public ResponseResult selectMedia(TeachplanMedia teachplanMedia)
    {
        //根据id判断是否为小章节
        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
        if(teachplanOptional.isPresent())
        {
            Teachplan teachplan = teachplanOptional.get();
            if(!teachplan.getGrade().equals("3"))
                ExceptionCast.cast(CourseCode.COURSE_MEDIS_SELECTERROR);
        }


        //给小章节保存关联视频
        TeachplanMedia save = teachplanMediaRepository.save(teachplanMedia);
        return new ResponseResult(CommonCode.SUCCESS);

    }
}
