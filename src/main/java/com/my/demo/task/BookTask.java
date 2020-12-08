package com.my.demo.task;

import com.my.demo.utils.BookDownProcess;
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
    @Scheduled(cron = "0 0 0 * * ?")
    private void fun(){
        try {
            log.info("===================Task Book start========================");
            new BookDownProcess().start();
            log.info("===================Task Book end========================");
        }catch (Exception e){
            log.error("BookTask error",e);
        }
    }
}
