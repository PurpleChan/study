package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/13
 * @Description:PACKAGE_NAME
 * @version:1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestElasticSearch
{
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RestClient restClient;

    //搜索type下的所有记录
    @Test
    public void test_searchAll() throws IOException
    {
        SearchRequest xc_course = new SearchRequest("xc_course");

        xc_course.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});

        xc_course.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(xc_course);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] hits1 = hits.getHits();

        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }

    //分页查询
    @Test
    public void test_fenye() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        System.out.println(hits1.length);
    }

    //精确查询  不将搜索词分词
    @Test
    public void test_TermQuery() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});

        //searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        String[] ids = new String[]{"1", "2"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }

    //matchquery  将要搜索的关键字先拆词  然后根据要求(and[所有词匹配才符合],or[只要有一个词匹配就符合]...)等匹配
    @Test
    public void test_matchquery() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});

        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "spring开发").operator(Operator.OR));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }


    //multi query  可以匹配多个字段  还可以给某个字段的符合度增加权重
    @Test
    public void test_multiquery() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});

        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring框架", "name", "description").field("name",10));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }


    //布尔查询  可以将多个查询组合起来
//    must：表示必须，多个查询条件必须都满足。（通常使用must）
//    should：表示或者，多个查询条件只要有一个满足即可。
//    must_not：表示非。
    @Test
    public void test_booleanquery() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});

        TermQueryBuilder termQuery = QueryBuilders.termQuery("studymodel", 20101);
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery("spring", "name", "description");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(termQuery).must(multiMatchQuery);

        searchSourceBuilder.query(boolQuery);


        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }


    //过滤器   是针对搜索的结果进行过虑   性能比查询要高
    @Test
    public void test_filter() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        searchSourceBuilder.query(boolQuery);



        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }


    //排序   可以在字段上添加一个或多个排序，支持在keyword、date、float等类型上添加，text类型的字段上不允许添加排
    @Test
    public void test_sort() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        //排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.ASC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));

        searchSourceBuilder.query(boolQuery);



        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();

            Map<String, Object> result = hit.getSourceAsMap();
            String name = (String)result.get("name");
            String studymodel = (String)result.get("studymodel");
            String description = (String)result.get("description");
        }
    }


    //高亮显示
    @Test
    public void test_highlight() throws IOException
    {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        //排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.ASC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));

        searchSourceBuilder.query(boolQuery);

        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit: hits1)
        {
            Map<String, Object> res = hit.getSourceAsMap();
            String name = (String)res.get("name");

            //取出高亮内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null)
            {
                HighlightField name1 = highlightFields.get("name");
                if(name1!=null)
                {
                    Text[] fragments = name1.getFragments();
                    StringBuilder stringBuilder = new StringBuilder();
                    for(Text fragment:fragments)
                    {
                        stringBuilder.append(fragment.string());
                    }
                    name=stringBuilder.toString();
                }
            }

        }
    }

}
