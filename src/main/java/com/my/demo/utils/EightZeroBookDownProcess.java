package com.my.demo.utils;

import com.my.demo.content.BookEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨宇帆
 * @create 2020-12-14
 */
public class EightZeroBookDownProcess implements PageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EightZeroBookDownProcess.class);
    private Site site = Site.me().setTimeOut(5000).setRetryTimes(3).setSleepTime(1000);
    private static final String EightZeroUrl = "https://www.txt80.com";


    @Override
    public void process(Page page) {
        List<String> list = page.getHtml().xpath("//div[@class='list_l_box']/div[@class='slist']/div[@class='info']/h4/a/@href").all();
        if(!CollectionUtils.isEmpty(list)){
            //详情页连接发现
            List<String> urls = list.stream().map(url -> EightZeroUrl + url).collect(Collectors.toList());
            page.addTargetRequests(urls);
        }
        //详情数据

        page.addTargetRequests(page.getHtml().links().regex(BookEnum.EightZero.getLikeUrl()).all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new EightZeroBookDownProcess())
                .addUrl(BookEnum.EightZero.getBaseUrl())
                .thread(5)
                .run();
    }

    public void start(){
        logger.info("================EightZeroBookDownProcess.start===============");
        Spider.create(new EightZeroBookDownProcess())
                .addUrl(BookEnum.EightZero.getBaseUrl())
                .thread(5)
                .run();
        logger.info("================EightZeroBookDownProcess.end===============");
    }
}
