package com.my.demo.utils;

import com.my.demo.content.BookEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author 杨宇帆
 * @create 2020-12-07
 */
@Service
public class BookDownProcess implements PageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BookDownProcess.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public static void main(String[] args) {
        Spider.create(new BookDownProcess())
                .addUrl(BookEnum.qishu.getBaseUrl())
                .thread(5)
                .run();
    }


    @Override
    public void process(Page page) {
        try {
            String name = page.getHtml().xpath("//dl[@id='downInfoArea'/dt[@id='downInfoArea'/dt/text()]").get();
            logger.info("===================================="+ name);
//            String name = page.getHtml().xpath("//div[@class='detail_right']/h1/text()").get();
//            String url = page.getUrl().get();
//            String hits = page.getHtml().xpath("//div[@class='detail_right']/ul/li[1]/text()").get();
//            String size = page.getHtml().xpath("//div[@class='detail_right']/ul/li[2]/text()").get();
//            String updateTime = page.getHtml().xpath("//div[@class='detail_right']/ul/li[4]/text()").get();
//            String status = page.getHtml().xpath("//div[@class='detail_right']/ul/li[5]/text()").get();
//            String author = page.getHtml().xpath("//div[@class='detail_right']/ul/li[6]/text()").get();
//            String info = page.getHtml().xpath("//div[contains(@class,'showBox') and contains(@class,'mt20')]/div[@class='showInfo']/p/text()").get();
//            String latestchapter = page.getHtml().xpath("//div[@class='detail_right']/ul//li[8]/a/text()").get();
//            String type = page.getHtml().xpath("//div[contains(@class,'wrap') and contains(@class,'position')]/span/a[2]/text()").get();
//
//
//
//            String[] urls = url.split("/");
//            String[] oherId = urls[urls.length - 1].split(".html");
//            String[] authorList = author.trim().split("：");
//            String[] hitsList = hits.trim().split("：");
//            String[] sizeList = size.trim().split("：");
//            String[] updateTimeList = updateTime.trim().split("更新日期：");
//            String[] statusList = status.trim().split("：");
//
//            Book book = new Book();
//            book.setCreatetime(new Date());
//            book.setUpdatetime(new Date());
//            book.setDeleted(0L);
//            book.setName(name);
//            book.setUrl(url);
//            book.setHits(hitsList[hitsList.length-1]);
//            book.setSize(sizeList[sizeList.length-1]);
//            book.setLastupdatetime(updateTimeList[1]);
//            book.setStatus(statusList[statusList.length-1]);
//            book.setAuthor(authorList[authorList.length-1]);
//            book.setLntroduction(info);
//            book.setLatestchapter(latestchapter);
//            book.setSource(BookEnum.qishu.getSource());
//            book.setType(type);
//            book.setOtherId(oherId[1]);
//            book.setVersion(1);
//            IBookService iBookService = (IBookService) SpringContextUtil.getBean(IBookService.class);
//            iBookService.saveOrUpdate(book);


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
                .thread(1)
                .run();
        logger.info("================BookDownProcess.end===============");
    }

}
