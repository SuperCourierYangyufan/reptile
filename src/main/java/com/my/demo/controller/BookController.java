package com.my.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.my.demo.service.IBookService;
import com.my.demo.utils.BookDownProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yufan.yang
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private IBookService iBookService;
    @Autowired
    private BookDownProcess bookDownProcess;

    @GetMapping("/{id}")
    public String find(@PathVariable Long id){
        return JSONObject.toJSONString(iBookService.getById(id));
    }

    @GetMapping("/start")
    public String start(){
        try {
            bookDownProcess.start();
        }catch (Exception e){
            logger.error("BookController.start error",e);
            return e.getMessage();
        }
        return "success";
    }

}

