package com.zhixin.service;

import com.zhixin.entity.Book;
import com.zhixin.entity.ESindex;

import java.util.List;

public interface BookService {

    String findByname(String name);

    String findByauthor(String name);

    String findAll();

    void insertOne(String index, String type,  String id,String jsonStr);

    void updateById(String index, String type, String id, String jsonStr);

    void deleteById(String index, String type, String id);


}
