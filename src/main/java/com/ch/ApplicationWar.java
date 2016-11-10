package com.ch;

import com.ch.listener.MyListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 部署war到tomcat
 * Created by banmo.ch on 16/11/8.
 * Email: banmo.ch@alibaba-inc.com
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ApplicationWar extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationWar.class);
    }
//    public static void main(String[] args) {
//        SpringApplication.run(ApplicationWar.class,args);
//    }
}