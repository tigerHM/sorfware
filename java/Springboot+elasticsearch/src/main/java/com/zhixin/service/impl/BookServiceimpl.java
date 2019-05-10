package com.zhixin.service.impl;

import com.zhixin.service.BookService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BookServiceimpl implements BookService {

    @Resource
    TransportClient transportClient;

    //根据名称查询  精准查询
    @Override
    public String findByname(String name) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("bookname", name);
        SearchResponse searchResponse = transportClient.prepareSearch("book").setTypes("info").setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(matchQueryBuilder)
                .execute().actionGet();

        SearchHits hits = searchResponse.getHits();

        StringBuilder stringBuilder = new StringBuilder();

        String str=null;
        if(hits.totalHits==1){
            String sourceAsString = hits.getAt(0).getSourceAsString();
            return sourceAsString;
        } else {
            for (SearchHit h : hits) {
                stringBuilder.append(h.getSourceAsString()+",");
            }
            str=stringBuilder.toString();

            str=str.substring(0,str.length()-1);
        }
        return "["+str+"]";
    }
    //根据查询  精准查询
    @Override
    public String findByauthor(String name) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("author", name);
        SearchResponse searchResponse = transportClient.prepareSearch("book").setTypes("info").setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(matchQueryBuilder)
                .execute().actionGet();

        SearchHits hits = searchResponse.getHits();

        StringBuilder stringBuilder = new StringBuilder();
        String str=null;
        if(hits.totalHits==1){
            String sourceAsString = hits.getAt(0).getSourceAsString();
            return sourceAsString;
        } else {
            for (SearchHit h : hits) {
                stringBuilder.append(h.getSourceAsString()+",");
            }
            str=stringBuilder.toString();

            str=str.substring(0,str.length()-1);
        }
        return "["+str+"]";
    }

    //查询所有 matchAllQuery
    @Override
    public String findAll() {
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

        SearchResponse searchResponse = transportClient.prepareSearch("book").setTypes("info").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchAllQueryBuilder).execute().actionGet();

        SearchHits hits = searchResponse.getHits();

        StringBuilder stringBuilder = new StringBuilder();
        String str=null;
        for (SearchHit h:hits) {
            stringBuilder.append(h.getSourceAsString());
        }
        str=stringBuilder.toString();
        str=str.substring(0,str.length()-1);
        return "["+str+"]";
    }

    //插入一条数据
    @Override
    public void insertOne(String index, String type, String id,String jsonStr) {

        IndexResponse indexResponse = transportClient.prepareIndex(index, type, id).setSource(jsonStr, XContentType.JSON).get();
        System.out.println(indexResponse.status());
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
