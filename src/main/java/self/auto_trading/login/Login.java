package self.auto_trading.login;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import self.auto_trading.Globe;
import self.auto_trading.HttpHeadSet;
import self.auto_trading.MyHttpClient;
import self.auto_trading.bean.Coin;
import self.auto_trading.data.impl.TradeInfo;
import self.auto_trading.service.RealTimeTrade;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
 *Login.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class Login {
    public static void main(String[] args) throws InterruptedException {

        new RealTimeTrade(new Coin("read_usdt", 0, 10)).tradeStart();
//        login();
//        System.out.println(TradeInfo.getTradeData("read_usdt").get(0).getPrice());
//        TradeInfo.revoke("read_usdt","351939");

//        TradeInfo.trade(Globe.SELL, "read_usdt", 0.7f, 20f);
        System.out.println();
    }

    public static boolean login() {
        try {
            getCookie();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        HttpPost post = MyHttpClient.createHttpPost("https://www.okex.com/user/login/index.do?random=71", HttpHeadSet.Login);

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("loginName",""));
        formParams.add(new BasicNameValuePair("password",""));
        formParams.add(new BasicNameValuePair("weixinBind", "0"));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            post.setEntity(entity);
            CloseableHttpResponse response = MyHttpClient.getInstance(true).execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity()));

                if (result.getInteger("resultCode") != 0) {
                    System.out.println("--------登陆失败,还剩" + result.getInteger("errorNum") + "次机会!");
                    return false;
                }

                Header[] headers = response.getHeaders("Set-Cookie");
                for (Header header : headers) {
                    String[] headerSplit = header.getValue().split(";");
                    for (String s : headerSplit) {
                        if (s.startsWith("coin_session_id_o")) {
                            MyHttpClient.setUserId(s);
                        }
                    }
                }
                EntityUtils.consume(response.getEntity());
                System.out.println("--------登陆成功");
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void getCookie() throws IOException {
        HttpGet httpGet = new HttpGet("https://www.okex.com/");
        CloseableHttpResponse response = MyHttpClient.getInstance(true).execute(httpGet);
        setCookie(response);
        EntityUtils.consume(response.getEntity());
    }

    private static void setCookie(CloseableHttpResponse response) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                String[] headerSplit = header.getValue().split(";");
                for (String s : headerSplit) {
                    if (s.startsWith("JSESSIONID")) {
                        MyHttpClient.setSessionId(s);
                    }
                }
            }
        }
    }
}
