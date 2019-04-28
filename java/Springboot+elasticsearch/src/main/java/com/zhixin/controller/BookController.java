package com.zhixin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhixin.entity.Book;
import com.zhixin.service.impl.BookServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    BookServiceimpl bookService;

    @RequestMapping("/insert")
    public String insertOne(){
        Book book = new Book(new Integer("10000"),"格林童话","格林兄弟");
        String jsonStr = JSON.toJSONString(book, SerializerFeature.WriteDateUseDateFormat);
        bookService.insertOne("book","info","000001",jsonStr);

        return "success";
    }

    @RequestMapping("/hello")
    public String hello(){

        return "hello tomcat";
    }

}
