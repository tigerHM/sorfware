import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

public class test {
        public static void main(String[] args) {

            try {
                Settings settings = Settings.builder().put("cluster.name","es").build();

                TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.4.16"), 9300));
                //创建索引，设置分词
//                CreateIndexRequest indexRequest = new CreateIndexRequest("messsage");
//
//                indexRequest.settings(Settings.builder().put("analysis.analyzer.default.tokenizer","ik_smart"));
//                transportClient.admin().indices().create(indexRequest).actionGet();

//                //插入文档
//                IndexRequest request=new IndexRequest("messsage","info");
//                Messsage messsage=new Messsage(5,"ANTA","2019-05-03 22:23:10");
//                String jsonStr = JSON.toJSONString(messsage, SerializerFeature.WriteDateUseDateFormat);
//                request.source(jsonStr);
//                transportClient.index(request).actionGet();

                //matchPhraseQuery短语查询
//                SearchRequestBuilder requestBuilder = transportClient.prepareSearch("messsage");
//                MatchPhraseQueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("_all", "大米");
                //查询指定索引所有文档
//                SearchRequestBuilder requestBuilder = transportClient.prepareSearch("messsage");
//                MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
                //纠错查询  在中文使用中匹配结果不是很好
//                SearchRequestBuilder requestBuilder = transportClient.prepareSearch("messsage");
//                FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("belong", "米").fuzziness(Fuzziness.AUTO).maxExpansions(50).transpositions(true);

                //multiMatchQuery实现模糊查询和精确查询（常用）“_all”表明所有字段
                SearchRequestBuilder requestBuilder = transportClient.prepareSearch("messsage");
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("AN", "_all");

                SearchResponse searchResponse = requestBuilder.setQuery(multiMatchQueryBuilder).execute().actionGet();
                SearchHits hits = searchResponse.getHits();
                for (SearchHit h:hits) {
                    System.out.println(h.getSourceAsString());
                }

            } catch (UnknownHostException e){
                e.printStackTrace();
            }

        }
}

