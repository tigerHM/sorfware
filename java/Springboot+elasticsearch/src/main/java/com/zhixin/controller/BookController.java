package com.zhixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhixin.entity.Book;
import com.zhixin.service.impl.BookServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    BookServiceimpl bookService;

    @RequestMapping("/insert")
    public String insertOne(){
        Book book = new Book(new Integer("10011"),"呐喊","鲁迅");
        String jsonStr = JSON.toJSONString(book, SerializerFeature.WriteDateUseDateFormat);
        //前端传递过来的是json数据
        bookService.insertOne("book","info","000003",jsonStr);
        return "success";

    }


    @RequestMapping ("/findone/{name}")
    public String findByname(@PathVariable  String name){

        String all = bookService.findByname(name);

        return all;

    }

    @RequestMapping ("/findbyauthor/{name}")
    public String findByauthor(@PathVariable  String name){

        String all = bookService.findByauthor(name);

        return all;

    }

    @RequestMapping ("/findall")
    public String findall(){

        String all = bookService.findAll();

        return all;
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello tomcat";
    }
}
