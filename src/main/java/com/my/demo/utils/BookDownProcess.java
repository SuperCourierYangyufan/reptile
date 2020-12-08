package com.my.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.my.demo.content.BookEnum;
import com.my.demo.content.StatusEnum;
import com.my.demo.entity.Book;
import com.my.demo.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;

/**
 * @author 杨宇帆
 * @create 2020-12-07
 */
@Service
public class BookDownProcess implements PageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BookDownProcess.class);
    private Site site = Site.me().setTimeOut(5000).setRetryTimes(3).setSleepTime(1000);



    @Override
    public void process(Page page) {
        try {
            Book book = new Book();
            book.setCreatetime(new Date());
            book.setUpdatetime(new Date());
            book.setDeleted(0L);
            book.setUrl(page.getUrl().toString());
            String name = page.getHtml().xpath("//dt[@id='downInfoTitle']/text()").get();
            book.setName(name);
            String type = page.getHtml().xpath("//div[@id='mainBody']/div[@class='pageMainArea']/dl/dd[@class='downInfoRowL']/text(5)").get();
            String author = page.getHtml().xpath("//div[@id='mainBody']/div[@class='pageMainArea']/dl/dd[@class='downInfoRowL']/text(7)").get();
            String size = page.getHtml().xpath("//div[@id='mainBody']/div[@class='pageMainArea']/dl/dd[@class='downInfoRowL']/text(9)").get();
            String lastupdatetime = page.getHtml().xpath("//div[@id='mainBody']/div[@class='pageMainArea']/dl/dd[@class='downInfoRowL']/text(13)").get();
            String starUrl = page.getHtml().xpath("//div[@id='mainBody']/div[@class='pageMainArea']/dl/dd[@class='downInfoRowL']/img/@src").get();
            String[] starUrlList = starUrl.split("/");
            String hits = starUrlList[starUrlList.length - 1].split("\\.")[0];
            book.setHits(hits);
            book.setType(type);
            book.setAuthor(author);
            book.setSize(size);
            book.setLastupdatetime(lastupdatetime);
            String lntroduction = page.getHtml().xpath("div[@id='mainSoftIntro']/p[2]/text()").get();
            book.setLntroduction(lntroduction);
            book.setStatus(StatusEnum.unknown.getCode());
            book.setVersion(1);
            book.setSource(BookEnum.qishu.getSource());
            String[] urlSplit = page.getUrl().toString().split("/");
            String otherId = urlSplit[urlSplit.length - 1].split("\\.")[0];
            book.setOtherId(otherId);
            logger.info("====================================存储对象:"+ JSONObject.toJSONString(book)+"=========================");
//            String type = page.getHtml().xpath("//div[contains(@class,'wrap') and contains(@class,'position')]/span/a[2]/text()").get();



            IBookService iBookService = (IBookService) SpringContextUtil.getBean(IBookService.class);
            iBookService.saveOrUpdate(book);


            page.addTargetRequests(page.getHtml().links().regex(BookEnum.qishu.getLikeUrl()).all());
        }catch (Exception e){
            logger.error("BookDownProcess.down error",e);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void start() throws Exception{
        logger.info("================BookDownProcess.start===============");
        Spider.create(new BookDownProcess())
                .addUrl(BookEnum.qishu.getBaseUrl())
                .thread(5)
                .run();
        logger.info("================BookDownProcess.end===============");
    }

}
