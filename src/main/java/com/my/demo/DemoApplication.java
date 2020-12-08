package com.my.demo;

import com.my.demo.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.my.demo.mapper")
@EnableScheduling
public class DemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        SpringContextUtil.setApplicationContext(context);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        SpringContextUtil.setApplicationContext(builder.context());
        return builder.sources(DemoApplication.class);
    }
}
