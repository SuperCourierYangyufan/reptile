package com.my.demo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.demo.entity.Book;
import com.my.demo.service.IBookService;
import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
}
