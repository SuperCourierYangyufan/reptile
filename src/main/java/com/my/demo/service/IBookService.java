package com.my.demo.service;

import com.my.demo.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yufan.yang
 * @since 2020-12-07
 */
public interface IBookService extends IService<Book> {

    void start(String url) throws Exception;
}
