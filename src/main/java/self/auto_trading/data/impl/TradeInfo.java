package self.auto_trading.data.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import self.auto_trading.Globe;
import self.auto_trading.HttpHeadSet;
import self.auto_trading.bean.Trade;
import self.auto_trading.bean.PriceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *TradeInfo.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class TradeInfo {
    private static final String TRADE_INFO = "https://www.okex.com/v2/orders/unsettlement?page=1&perPage=10&symbol=CODE&systemType=1&entrustType=tenNodeal";
    private static final String PRICE_LIST = "https://www.okex.com/api/v1/depth.do?symbol=CODE&size=DEEP";
    private static final String TRADE = "https://www.okex.com/v2/orders/CODE";
    private static final String REVOKE = "https://www.okex.com/v2/orders/CODE/ORDER_ID";


    /**
     * 我的交易记录(交易中)
     * @param code
     * @return
     */
    public static List<Trade> getTradeData(String code) {
        JSONObject info = JSON.parseObject(HttpData.getData(TRADE_INFO.replaceFirst("CODE", code), HttpHeadSet.TradeInfo));
        return info.getJSONObject("data").getJSONArray("orders").toJavaList(Trade.class);
    }

    /**
     * 交易列表
     * @param code
     * @param deep
     * @return
     */
    public static PriceList getPriceList(String code, int deep) {
        JSONObject info = JSON.parseObject(HttpData.getData(PRICE_LIST.replaceFirst("CODE", code).replaceFirst("DEEP",String.valueOf(deep)), HttpHeadSet.PriceList));

        JSONArray sell = info.getJSONArray("asks");
        List<Trade> sellList = new ArrayList<>(sell.size());
        for (int i = sell.size() - 1; i >= 0; i--) {
            JSONArray trade = sell.getJSONArray(i);
            sellList.add(new Trade(trade.getDouble(0), trade.getDouble(1), Globe.SELL));
        }

        JSONArray buy = info.getJSONArray("bids");
        List<Trade> buyList = new ArrayList<>(sell.size());
        for (int i = 0; i < buy.size(); i++) {
            JSONArray trade = buy.getJSONArray(i);
            buyList.add(new Trade(trade.getDouble(0), trade.getDouble(1), Globe.SELL));
        }

        return new PriceList(sellList, buyList);
    }

    /**
     * 买入,卖出
     * @param code
     * @return
     */
    public static boolean trade(int type,String code, double price, float num) {
        Map<String, Object> params = new HashMap<>();
        params.put("side", type);
        params.put("orderType", 0);
        params.put("price", price);
        params.put("size", num);
        params.put("source", 0);
        params.put("systemType", 1);
        params.put("tradePasswd", "");
        String result = HttpData.postData(TRADE.replaceFirst("CODE", code), params, HttpHeadSet.Trade);
        if (result != null) {
            JSONObject info = JSON.parseObject(result);
            if (info.getString("msg").trim().equals("")) {
                return true;
            }
            result = info.getString("code");
        }
        System.out.println("---------"+ (type == 1 ? "购买" : "卖出") + code + "失败! 错误代码:" + result);
        return false;
    }

    /**
     * 撤单
     * @param code
     * @param orderId
     * @return
     */
    public static boolean revoke(String code, String orderId) {
        String result = HttpData.deleteData(REVOKE.replaceFirst("CODE", code).replaceFirst("ORDER_ID", orderId), HttpHeadSet.Revoke);

        if (result != null) {
            JSONObject info = JSON.parseObject(result);
            if (info.getString("msg").equals("")) {
                return true;
            }
        }
        System.out.println("订单"+ code+ ":"+orderId + " 取消失败!");
        return false;
    }



    public static void main(String[] args) {
//        String txt = "[{\"averagePrice\":{},\"createTime\":1516251497000,\"executedValue\":\"0.0000\",\"filledSize\":\"0.00000000\",\"id\":329167,\"modifyTime\":1516251496000,\"notStrike\":{},\"orderType\":0,\"price\":\"0.1000\",\"productId\":\"0\",\"quoteSize\":\"0.0000\",\"side\":2,\"size\":\"50.00006760\",\"source\":0,\"status\":0,\"symbol\":{},\"systemType\":1,\"trunOver\":{},\"userId\":6945375}]";
//        List<MyTrade> trades = JSONArray.parseArray(txt,MyTrade.class);\




        PriceList trades = getPriceList("",1);

        System.out.println("==");

    }
}
