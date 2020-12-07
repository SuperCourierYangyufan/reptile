package com.my.demo.utils;

import com.my.demo.content.BookEnum;
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
    private String url;
    private Integer source;

    private static final Logger logger = LoggerFactory.getLogger(BookDownProcess.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);


    @Override
    public void process(Page page) {
        String name = page.getHtml().xpath("//div[@class='detail_right']/h1/text()").get();
        String url = page.getUrl().get();
        String author = page.getHtml().xpath("//div[@class='detail_right']/ul/li[6]/text()").get();
        String[] urls = url.split("/");
        String[] oherId = urls[urls.length - 1].split(".html");

        Book book = new Book();
        book.setCreatetime(new Date());
        book.setUpdatetime(new Date());
        book.setName(name);
        book.setUrl(url);
        book.setAuthor(author);
        book.setOtherId(oherId[1]);
        IBookService iBookService = (IBookService) SpringContextUtil.getBean(IBookService.class);
        iBookService.saveOrUpdate(book);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void start() throws Exception{
        logger.error("================BookDownProcess.start===============");
        Spider.create(new BookDownProcess(BookEnum.qishu.getBaseUrl(), BookEnum.qishu.getSource()))
                .addUrl(BookEnum.qishu.getBaseUrl())
                .thread(5)
                .run();
        logger.error("================BookDownProcess.end===============");
    }

    public BookDownProcess() {
    }

    public BookDownProcess(String url, Integer source) {
        this.url = url;
        this.source = source;
    }

    public static Logger getLogger() {
        return logger;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
