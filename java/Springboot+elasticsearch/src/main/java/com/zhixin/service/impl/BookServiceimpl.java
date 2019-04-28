package com.zhixin.service.impl;

import com.zhixin.entity.Book;
import com.zhixin.entity.ESindex;
import com.zhixin.service.BookService;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookServiceimpl implements BookService {

    @Resource
    TransportClient transportClient;


    @Override
    public Book findByid(ESindex es, String id) {
        return null;
    }

    @Override
    public List<Book> findAll(ESindex es) {
        return null;
    }

    @Override
    public void insertOne(String index, String type, String id,String jsonStr) {
        transportClient.prepareIndex(index, type,id).setSource(jsonStr, XContentType.JSON).get();
    }

    @Override
    public void updateById(String index, String type, String id, String jsonStr) {
        transportClient.prepareUpdate(index, type,id)
                .setDoc(jsonStr, XContentType.JSON)
                .get();
    }

    @Override
    public void deleteById(String index, String type, String id) {
        transportClient.prepareDelete(index, type, id).get();
    }
}
