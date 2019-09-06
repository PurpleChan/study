package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.response.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/14
 * @Description:com.xuecheng.search.service
 * @version:1.0
 */
@Service
public class EsCourseService
{
    @Value("${xuecheng.elasticsearch.course.index}")
    private String index;

    @Value("${xuecheng.elasticsearch.course.type}")
    private String type;

    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    @Value("${xuecheng.elasticsearch.media.index}")
    private String media_index;

    @Value("${xuecheng.elasticsearch.media.type}")
    private String media_type;

    @Value("${xuecheng.elasticsearch.media.source_field}")
    private String media_source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RestClient restClient;


    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam)
    {
        if(page==0)
            page=1;
        if(size==0)
            size=9;

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(source_field.split(","), new String[]{});
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //关键字匹配
        String keyword = courseSearchParam.getKeyword();
        if(StringUtils.isNotEmpty(keyword))
        {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "name", "description", "teachplan").field("name", 10).field("teachplan", 5);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //过滤
        String mt = courseSearchParam.getMt();
        if(StringUtils.isNotEmpty(mt))
        {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", mt));
        }

        String st = courseSearchParam.getSt();
        if(StringUtils.isNotEmpty(st))
        {
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", st));
        }

        String grade = courseSearchParam.getGrade();
        if(StringUtils.isNotEmpty(grade))
        {
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", grade));
        }

        Float price_min = courseSearchParam.getPrice_min();
        if(price_min!=null)
        {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(Float.valueOf(price_min)));
        }

        Float price_max = courseSearchParam.getPrice_max();
        if(price_max!=null)
        {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(Float.valueOf(price_max)));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);

        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);


        SearchResponse searchResponse =null;
        try
        {
            searchResponse = restHighLevelClient.search(searchRequest);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        queryResult.setTotal(totalHits);

        SearchHit[] hits1 = hits.getHits();
        ArrayList<CoursePub> coursePubs = new ArrayList<>();
        for(SearchHit hit:hits1)
        {
            CoursePub coursePub=new CoursePub();
            Map<String, Object> map = hit.getSourceAsMap();
            coursePub.setName((String)map.get("name"));
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null)
            {
                HighlightField name = highlightFields.get("name");
                if(name!=null)
                {
                    Text[] fragments = name.getFragments();
                    StringBuilder stringBuilder=new StringBuilder();
                    for(Text fragment:fragments)
                    {
                        stringBuilder.append(fragment.string());
                    }
                    coursePub.setName(stringBuilder.toString());
                }
            }

            coursePub.setId((String)map.get("id"));
            coursePub.setUsers((String)map.get("users"));
            coursePub.setMt((String)map.get("mt"));
            coursePub.setSt((String)map.get("st"));
            coursePub.setGrade((String)map.get("studymodel"));
            coursePub.setTeachmode((String)map.get("teachmode"));
            coursePub.setDescription((String)map.get("description"));
            coursePub.setCharge((String)map.get("charge"));
            coursePub.setValid((String)map.get("valid"));
            coursePub.setQq((String)map.get("qq"));
            coursePub.setPrice((Double) map.get("price"));
            coursePub.setPrice_old((Double) map.get("price_old"));
            coursePub.setExpires((String)map.get("expires"));
            coursePub.setPic((String)map.get("pic"));
            coursePub.setTeachplan((String)map.get("teachplan"));
            coursePubs.add(coursePub);
        }

        queryResult.setList(coursePubs);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 根据id查询课程信息
     * @param id
     * @return
     */
    public Map<String, CoursePub> getall(String id)
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name", "grade", "charge","pic"}, new String[]{});
        TermQueryBuilder termQuery = QueryBuilders.termQuery("id", id);
        searchSourceBuilder.query(termQuery);
        searchRequest.source(searchSourceBuilder);

        try
        {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] hits1 = hits.getHits();
            HashMap<String, CoursePub> map = new HashMap<>();
            for(SearchHit hit:hits1)
            {
                CoursePub coursePub = new CoursePub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                coursePub.setName((String)sourceAsMap.get("name"));
                coursePub.setId(hit.getId());
                coursePub.setPic((String) sourceAsMap.get("pic"));
                coursePub.setGrade((String)sourceAsMap.get("grade"));
                coursePub.setTeachplan((String)sourceAsMap.get("teachplan"));
                coursePub.setDescription((String)sourceAsMap.get("description"));
                map.put(hit.getId(), coursePub);
            }
            return map;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据课程计划id查询媒资信息
     * @param teachplanIds
     * @return
     */
    public QueryResponseResult getMedia(String[] teachplanIds)
    {
        //设置索引
        SearchRequest searchRequest = new SearchRequest(media_index);
        //设置类型
        searchRequest.types(media_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        String[] source_fields = media_source_field.split(",");
        searchSourceBuilder.fetchSource(source_fields, new String[]{});
        //查询条件，根据课程计划id查询(可传入多个id)
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String,CoursePub> map = new HashMap<>();
        //数据列表
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            TeachplanMediaPub teachplanMediaPub =new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出课程计划媒资信息
            String courseid = (String) sourceAsMap.get("courseid");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_url = (String) sourceAsMap.get("media_url");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");
            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id);
            //将数据加入列表
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        //构建返回课程媒资信息对象
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
