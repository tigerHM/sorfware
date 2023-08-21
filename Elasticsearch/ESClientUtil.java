// 连接配置安全模式的ES集群（账号密码,https,ssl）

package org.example;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClientUtil {

    private static String username ="xxx";

    private static String password ="xxxx";

    private static String[] address = new String[]{"xxx:xxx"};

    private static final int ADDRESS_LENGTH = 2;


    //是否启用ssl 0否 1是
    private Integer enableSsl=1;


    public RestHighLevelClient restClient() {
        RestHighLevelClient restClient = null;
        try {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);

            HttpHost[] hosts = Arrays.stream(address)
                    .map(this::makeHttpHost)
                    .filter(Objects::nonNull)
                    .toArray(HttpHost[]::new);

            restClient = new RestHighLevelClient(
                    RestClient.builder(hosts)
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                    httpClientBuilder.disableAuthCaching();
                                    httpClientBuilder.setSSLStrategy(sessionStrategy);
                                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                                    httpClientBuilder.setKeepAliveStrategy((httpResponse, httpContext) -> TimeUnit.MINUTES.toMillis(3));
                                    return httpClientBuilder;
                                }
                            }).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                                @Override
                                public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                                    return requestConfigBuilder.setConnectTimeout(5000 * 1000)
                                            .setSocketTimeout(6000 * 1000);//套接字超时，默认30s
                                }
                            }) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restClient;
    }

    /**
     * 处理请求地址
     * @param s
     * @return HttpHost
     */
    private HttpHost makeHttpHost(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String httpScheme = "http";
        if (enableSsl == 1){
            httpScheme = "https";
        }
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, httpScheme);
        } else {
            return null;
        }
    }
}
