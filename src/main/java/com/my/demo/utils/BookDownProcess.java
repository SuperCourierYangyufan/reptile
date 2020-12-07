package com.my.demo.utils;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author 杨宇帆
 * @create 2020-12-07
 */
@Component
public class BookDownProcess implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);


    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }

    public void start(String url) throws Exception{
        Spider.create(new BookDownProcess())
                .addUrl(url)
                .thread(5)
                .run();
    }
}
