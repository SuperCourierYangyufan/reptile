package com.my.demo.task;

import com.my.demo.utils.BookDownProcess;
import com.my.demo.utils.EightZeroBookDownProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 杨宇帆
 * @create 2020-12-08
 */
@Component
@Slf4j
public class BookTask {
    @Scheduled(cron = "0 0 16 * * ?")
    private void fun(){
        try {
            log.info("===================Task BookDownProcess start========================");
            new BookDownProcess().start();
            log.info("===================Task BookDownProcess end========================");
        }catch (Exception e){
            log.error("BookDownProcess error",e);
        }
    }



    @Scheduled(cron = "0 0 8 * * ?")
    private void fun1(){
        try {
            log.info("===================Task EightZeroBookDownProcess start========================");
            new EightZeroBookDownProcess().start();
            log.info("===================Task EightZeroBookDownProcess end========================");
        }catch (Exception e){
            log.error("EightZeroBookDownProcess error",e);
        }
    }
}
