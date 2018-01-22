package self.auto_trading;

import org.apache.http.client.methods.HttpRequestBase;

/*
 *HttpHeader.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public enum HttpHeadSet {

    Login {
        @Override
        public void setHeader(HttpRequestBase header) {
            setBaseHeader(header);
            header.setHeader("Accept", "*/*");
            header.setHeader("Cache-Control", "max-age=0");
            header.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            header.setHeader("devid", "cf8cc9e4-3dec-4acb-a3be-0456a6b5a1b8");
            header.setHeader("Host", "www.okex.com");
            header.setHeader("Upgrade-Insecure-Requests", "1");
            header.setHeader("Referer", "https://www.okex.com/");
            header.setHeader("X-Requested-With", "XMLHttpRequest");
        }
    },
    TradeInfo {
        @Override
        public void setHeader(HttpRequestBase header) {
            setBaseHeader(header);
            header.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            header.setHeader("Cache-Control", "max-age=0");
            header.setHeader("Host", "www.okex.com");
            header.setHeader("Upgrade-Insecure-Requests", "1");
        }
    },
    PriceList {
        @Override
        public void setHeader(HttpRequestBase header) {
            TradeInfo.setHeader(header);
        }
    },
    Trade {
        @Override
        public void setHeader(HttpRequestBase header) {
            setBaseHeader(header);
            header.setHeader("Accept", "*/*");
            header.setHeader("Cache-Control", "max-age=0");
            header.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            header.setHeader("Host", "www.okex.com");
            header.setHeader("Referer", "https://www.okex.com/spot/trade/index.do");
        }
    },
    Revoke {
        @Override
        public void setHeader(HttpRequestBase header) {
            Trade.setHeader(header);
        }
    };

    private static void setBaseHeader(HttpRequestBase header) {
        header.setHeader("Accept-Encoding", "gzip, deflate, br");
        header.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        header.setHeader("Connection", "keep-alive");
        header.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0");

    }

    public abstract void setHeader(HttpRequestBase header);


}
