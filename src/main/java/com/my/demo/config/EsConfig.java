package com.my.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 杨宇帆
 * @create 2021-04-10
 */
@Configuration
public class EsConfig {
    @Bean
    public RestHighLevelClient initRestHighLevelClient(){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("120.79.10.101",9200)));
        return restHighLevelClient;
    }
}
