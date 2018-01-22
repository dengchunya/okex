package self.auto_trading;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/*
 *MyHttpClient.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class MyHttpClient {
    private volatile static CloseableHttpClient client;
    private static String userId;
    private static String sessionId;
    private static RequestConfig config = RequestConfig
            .custom()
            .setConnectionRequestTimeout(5000)
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .build();

    public static CloseableHttpClient getInstance(boolean isSSL) {
        if (client == null) {
            synchronized (MyHttpClient.class) {
                if (client == null) {
                    if (isSSL) {
                        try {
                            client = createSSLClientDefault();
                        } catch (Exception e) {
                            client = HttpClients.createDefault();
                            e.printStackTrace();
                        }
                    }else {
                        client = HttpClients.createDefault();
                    }
                }
            }
        }
        return client;
    }

    private static CloseableHttpClient createSSLClientDefault() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext;

        sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }


    public static void setUserId(String userId) {
        MyHttpClient.userId = userId;
    }

    public static void setSessionId(String sessionId) {
        MyHttpClient.sessionId = sessionId;
    }


    public static HttpPost createHttpPost(String url, HttpHeadSet headSet) {
        HttpPost httpPost = new HttpPost(url);
        setHeader(httpPost, headSet);
        return httpPost;
    }

    public static HttpGet createHttpGet(String url, HttpHeadSet headSet) {
        HttpGet httpGet = new HttpGet(url);
        setHeader(httpGet, headSet);
        return httpGet;
    }

    public static HttpDelete createHttpDelete(String url, HttpHeadSet headSet) {
        HttpDelete httpDelete = new HttpDelete(url);
        setHeader(httpDelete, headSet);
        return httpDelete;
    }


    private static void setHeader(HttpRequestBase base, HttpHeadSet headSet) {
        base.setConfig(config);
        headSet.setHeader(base);
        String cookie = "";
        if (sessionId != null) {
            cookie += sessionId;
        }

        if (userId != null) {
            cookie += ";" + userId;
        }


        base.setHeader("Cookie", "coin_session_id_o=3c2b84bc-0a4f-4813-8229-50f0238bb75a29e4daddb9594f49; JSESSIONID=D66A57C6DB90E4A2E3A2294CF53B3D84;");
    }
}
