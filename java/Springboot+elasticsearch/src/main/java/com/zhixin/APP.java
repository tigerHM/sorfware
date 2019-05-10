package com.zhixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序入口
 * @
 */

@EnableAutoConfiguration
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.zhixin.*"})
public class APP {
    public static void main(String[] args) {


        SpringApplication.run(APP.class,args);
        System.out.println("程序正在运行...");
    }
}
