package com.my.demo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demo.entity.Book;
import com.my.demo.service.IBookService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author 杨宇帆
 * @create 2021-04-10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private IBookService iBookService;

    //创建索引
    @Test
    public void initIndex() throws Exception{
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(new CreateIndexRequest("book"), RequestOptions.DEFAULT);
        boolean isSuccess = createIndexResponse.isAcknowledged();
        System.out.println("索引操作"+isSuccess);
    }

    //查询索引
    @Test
    public void searchIndex() throws Exception{
        GetIndexRequest request = new GetIndexRequest();
        request.indices("book");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(getIndexResponse));
    }

    //删除索引
    @Test
    public void deletedIndex() throws Exception{
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(new DeleteIndexRequest("book"), RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(acknowledgedResponse));
    }

    //保存数据
    @Test
    public void saveBook() throws Exception{
        long id = RandomUtils.nextLong(1336577267591069697L, 1351439287968137218L);
        Book book = iBookService.getOne(new QueryWrapper<Book>().lambda().gt(Book::getId, id), false);
        System.out.println(JSONObject.toJSONString(book));

        //新增
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("book").id(book.getId().toString()).type("book");
        String json = JSONObject.toJSONString(book);
        indexRequest.source(json, XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(index));

        //更新流程
        UpdateRequest updateRequest = new UpdateRequest("book","book",book.getId().toString());
        updateRequest.doc(new HashMap(){{
            put("name","测试修改");
        }});
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(update));
    }

    //查询数据,byId
    @Test
    public void getBook() throws Exception{
        GetRequest getRequest = new GetRequest("book","book","1351439260088598529");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String json = getResponse.getSourceAsString();
        if(!StringUtils.isEmpty(json)) System.out.println(JSONObject.parseObject(json,Book.class));
    }

    //删除数据
    @Test
    public void deletedBook() throws Exception{
        DeleteRequest deleteRequest = new DeleteRequest("book","book","1351439260088598529");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(deleteResponse));
    }

    //批量
    @Test
    public void batchSaveBook() throws Exception{
        long id = RandomUtils.nextLong(1336577267591069697L, 1351439287968137218L);
        Page<Book> page = iBookService.page(new Page<>(0, 10), new QueryWrapper<Book>().lambda().gt(Book::getId, id));
        BulkRequest bulkRequest = new BulkRequest();
        for(Book book:page.getRecords()){
            IndexRequest indexRequest = new IndexRequest("book","book",book.getId().toString());
            indexRequest.source(JSONObject.toJSONString(book),XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
    }

    //查询
    @Test
    public void searchBook() throws Exception{
        SearchRequest searchRequest = new SearchRequest("book");
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("type","武侠仙侠"))
                .mustNot(QueryBuilders.matchQuery("author","酥酥麻麻"))
                .should(QueryBuilders.matchQuery("name","我真不想当圣师"))
                .must(QueryBuilders.rangeQuery("createtime").gte(1610668803000L))
                //设定模糊查询,可以设置偏差距离
//                .must(QueryBuilders.fuzzyQuery("name","诸天打?系统").fuzziness(Fuzziness.ONE))
                )
                .from(0).size(10)
                .sort("id", SortOrder.DESC)
                .fetchSource(new String[]{"id","name","author"},new String[]{})
                //高亮
                .highlighter(new HighlightBuilder(){{
                    preTags("<font color='red'>");
                    postTags("</font>");
                    field("name");
                }}));

        //聚合查询
//        searchRequest.source(new SearchSourceBuilder()
//                .aggregation(AggregationBuilders.max("maxTime").field("createtime")));
        //分组查询
//        searchRequest.source(new SearchSourceBuilder()
//                .aggregation(AggregationBuilders.terms("versionGroup").field("version")));
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for(SearchHit searchHit:search.getHits()){
            System.out.println(searchHit.getSourceAsString());
        }

        System.out.println(JSONObject.toJSONString(search.getAggregations()));
    }
}
