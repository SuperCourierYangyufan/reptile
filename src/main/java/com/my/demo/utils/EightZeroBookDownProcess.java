package com.my.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.demo.content.BookEnum;
import com.my.demo.entity.Book;
import com.my.demo.service.IBookService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
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
        String name = page.getHtml().xpath("//dd[@class='bt']/h2/text()").get();
        if(!StringUtils.isEmpty(name)){
            String url = page.getUrl().get();
            String[] split = url.split("/");
            String[] other = split[split.length - 1].split("\\.");
            String otherId = other[0];

            IBookService iBookService = (IBookService) SpringContextUtil.getBean(IBookService.class);
            QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
            queryWrapper.eq("otherId",otherId);
            Book selectOne = iBookService.getBaseMapper().selectOne(queryWrapper);
            Book book;
            if(selectOne == null){
                book = new Book();
                book.setCreatetime(new Date());
                book.setVersion(1);
            }else{
                book = selectOne;
                if(book.getVersion() == null||book.getVersion()==0){
                    book.setVersion(1);
                }
                book.setVersion(book.getVersion()+1);
            }

            //详情数据
            String author = page.getHtml().xpath("//div[@class='nrlist']/dl/dd[@class='db'][2]/a/text()").get();
            String status = page.getHtml().xpath("//div[@class='nrlist']/dl/dd[@class='db'][3]/span/text()").get();
            String type = page.getHtml().xpath("//div[@class='nrlist']/dl/dd[@class='db'][4]/a/text()").get();
            String size = page.getHtml().xpath("//div[@class='nrlist']/dl/dd[@class='db'][7]/span/text()").get();
            String lastupteTime = page.getHtml().xpath("//div[@class='nrlist']/dl/dd[@class='db'][8]/span/text()").get();
            String content = page.getHtml().xpath("//div[@class='cont']/text(2)").get();
            if(StringUtils.isEmpty(content.trim())){
                content = page.getHtml().xpath("//div[@class='cont']/p/text()").get();
            }

            book.setUpdatetime(new Date());
            book.setDeleted(0L);
            book.setName(name);
            book.setUrl(url);
            book.setAuthor(author);
            book.setSize(size);
            book.setLntroduction(content);
            book.setLastupdatetime(lastupteTime);
            book.setStatus(status);
            book.setType(type);
            book.setSource(BookEnum.EightZero.getSource());

            book.setOtherId(otherId);
            logger.info("====================================存储对象:"+ JSONObject.toJSONString(book)+"=========================");
            iBookService.saveOrUpdate(book);
        }
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
