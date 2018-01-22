package self.auto_trading;

import self.auto_trading.data.impl.RealTimeData;
import sun.misc.BASE64Encoder;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 *Data.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class Data {

    public static void main(String[] args) throws URISyntaxException, InterruptedException, UnsupportedEncodingException, NoSuchAlgorithmException {
        RealTimeData data = new RealTimeData("wss://real.okcoin.cn:10440/websocket/okcoinapi", array -> {
            System.out.println("--------------"+ array.toJSONString());
        });

        data.connection();
        while (true) {
            data.sendMsg("{'event':'addChannel','channel':'ok_spot_userinfo','parameters': {'api_key':'8adb4995-25ce-453c-8243-83fa8fa32edb','sign':'Njb+IiVQEMSFBwQ4bBGsWw=='}}");
            Thread.sleep(5000);
        }

//        qianMing();
    }

    private static String qianMing() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "{'api_key':'8adb4995-25ce-453c-8243-83fa8fa32edb','secret_key':'5C73644E26C18592AF6352545F57A857'}";
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        s = base64en.encode(md5.digest(s.getBytes("utf-8")));
        return s;
    }
}
