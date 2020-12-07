package com.my.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demo.entity.Book;
import com.my.demo.mapper.BookMapper;
import com.my.demo.service.IBookService;
import com.my.demo.utils.BookDownProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yufan.yang
 * @since 2020-12-07
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookDownProcess bookDownProcess;

    @Override
    public void start(String url) throws Exception {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    logger.error("================BookServiceImpl.start===============");
                    bookDownProcess.start(url);
                    logger.error("================BookServiceImpl.end===============");
                }catch (Exception e){
                    logger.error("BookServiceImpl.start error",e);
                }
            }
        });
    }
}
