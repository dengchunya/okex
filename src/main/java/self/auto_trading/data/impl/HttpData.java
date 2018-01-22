package self.auto_trading.data.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import self.auto_trading.HttpHeadSet;
import self.auto_trading.MyHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 *HttpData.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class HttpData {
    public static String getData(String url, HttpHeadSet header) {
        HttpGet httpGet = MyHttpClient.createHttpGet(url, header);
        HttpResponse response = null;
        try {
            response = MyHttpClient.getInstance(true).execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                EntityUtils.consume(response.getEntity());
                return result;
            }
        } catch (IOException e) {
            System.out.println("读取网址:" + url +" 时出错!");
            return null;
        }
        return null;
    }

    public static String postData(String url, Map<String, Object> params, HttpHeadSet headSet) {
        HttpPost post = MyHttpClient.createHttpPost(url, headSet);

        if (params != null && !params.isEmpty()) {
            List<NameValuePair> postParams = new ArrayList<>();
            params.forEach((key, value) -> {
                postParams.add(new BasicNameValuePair(key, value.toString()));
            });

            CloseableHttpResponse response = null;

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams, "UTF-8");
                post.setEntity(entity);
                response = MyHttpClient.getInstance(true).execute(post);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("读取网址:" + url +" 时出错!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取网址:" + url +" 时出错!");
            } finally {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String deleteData(String url, HttpHeadSet headSet) {
        HttpDelete httpDelete = MyHttpClient.createHttpDelete(url, headSet);
        HttpResponse response = null;
        try {
            response = MyHttpClient.getInstance(false).execute(httpDelete);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                EntityUtils.consume(response.getEntity());
                return result;
            }
        } catch (IOException e) {
            System.out.println("读取网址:" + url +" 时出错!");
            return null;
        }
        return null;
    }
}
