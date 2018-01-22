package self.auto_trading.data.impl;

import com.alibaba.fastjson.JSONArray;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import self.auto_trading.data.DataUpdateMonitor;

import java.net.URI;
import java.net.URISyntaxException;

/*
 *RealTimeData.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class RealTimeData {

    private WebSocketClient client;

    public RealTimeData(String url, DataUpdateMonitor monitor) throws URISyntaxException {
        this.client = new MyWebSocketClient(url, monitor);
    }

    public void closeConnection() {
        if (client != null && !client.isClosed() && !client.isClosing()) {
            client.close();
        }
    }

    public void connection () throws InterruptedException {
        if (client != null && !client.isConnecting()) {
            client.connectBlocking();
        }
    }

    public void sendMsg(String msg) {
        client.send(msg);
    }


    private static class MyWebSocketClient extends WebSocketClient{
        private DataUpdateMonitor monitor;

        public MyWebSocketClient(String url, DataUpdateMonitor monitor) throws URISyntaxException {
            super(new URI(url), new Draft_6455());
            this.monitor = monitor;
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            System.out.println("连接已经打开...");
        }

        @Override
        public void onMessage(String s) {
            if (monitor != null) {
                monitor.update(JSONArray.parseArray(s));
            }
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            System.out.println("连接已经关闭..." );
        }

        @Override
        public void onError(Exception e) {
            System.out.println("错误:" + e);
        }
    }
}
