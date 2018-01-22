package self.auto_trading.service;

import self.auto_trading.bean.Coin;
import self.auto_trading.bean.PriceList;
import self.auto_trading.bean.Trade;
import self.auto_trading.data.impl.TradeInfo;

import java.util.List;
import java.util.stream.Collectors;

import static self.auto_trading.Globe.BUY;
import static self.auto_trading.Globe.SELL;
import static self.auto_trading.Globe.SLEEP_TIME;

/*
 *RealTimeTrade.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class RealTimeTrade {
    private Coin coin;
    private double preBuyPrice = 0.0529;
    private int preStatus = SELL;


    public RealTimeTrade (Coin coin) {
        this.coin = coin;
    }


    public void tradeStart() throws InterruptedException {
        while (true) {
            List<Trade> list = TradeInfo.getTradeData(coin.getCode());
            list = list.stream().filter(trade -> trade.getSize() <= coin.getTradeNum()).collect(Collectors.toList());

            PriceList priceList = TradeInfo.getPriceList(coin.getCode(), 5);

            Trade myTrade;
            if (list.isEmpty()) {
                myTrade = new Trade();
                myTrade.setSide(preStatus == BUY ? SELL : BUY);
                myTrade.setPrice(Double.MAX_VALUE);
            } else {
                myTrade = list.get(0);
                List<Trade> tradeList = myTrade.getSide() == BUY ? priceList.getBuy() : priceList.getSell();
                if (coin.getIncrement() == 0) {
                    if (myTrade.getPrice() == tradeList.get(0).getPrice()) {
                        if (tradeList.get(0).getSize() != myTrade.getSize()) {
                            Thread.sleep(SLEEP_TIME);
                            continue;
                        }
                    }

                } else if(tradeList.get(1).getPrice() + (myTrade.getSide() == BUY ? 1 : -1) * coin.getIncrement() == myTrade.getPrice()) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }

            if (myTrade.getSide() == BUY) {
                if (priceList.getBuy().get(0).getPrice() + coin.getIncrement() < priceList.getSell().get(0).getPrice()
                        && priceList.getBuy().get(1).getPrice() - priceList.getBuy().get(0).getPrice() <= priceList.getSell().get(0).getPrice() - priceList.getBuy().get(0).getPrice()) {
                    boolean isSuccess = true;
                    if (myTrade.getId() != null) {
                        isSuccess = TradeInfo.revoke(coin.getCode(), String.valueOf(myTrade.getId()));
                        Thread.sleep(500);
                        priceList = TradeInfo.getPriceList(coin.getCode(), 5);
                    }
                    if (isSuccess) {
                        if (TradeInfo.trade(BUY, coin.getCode(), priceList.getBuy().get(0).getPrice() + coin.getIncrement(), coin.getTradeNum())) {
                            preBuyPrice = priceList.getBuy().get(0).getPrice() + coin.getIncrement();
                            preStatus = BUY;
                        }
                    }

                }
            } else {
                if (priceList.getSell().get(0).getPrice() - coin.getIncrement() > preBuyPrice
                        && priceList.getSell().get(1).getPrice() - priceList.getSell().get(0).getPrice() <= priceList.getSell().get(0).getPrice() - priceList.getBuy().get(0).getPrice()) {
                    boolean isSuccess = true;
                    if (myTrade.getId() != null) {
                        isSuccess = TradeInfo.revoke(coin.getCode(), String.valueOf(myTrade.getId()));
                        Thread.sleep(500);
                        priceList = TradeInfo.getPriceList(coin.getCode(), 5);
                    }
                    if (isSuccess) {
                        if (TradeInfo.trade(SELL, coin.getCode(), priceList.getSell().get(0).getPrice() - coin.getIncrement(), coin.getTradeNum())) {
                            preStatus = SELL;
                        }
                    }
                }
            }
            Thread.sleep(SLEEP_TIME);
        }
    }
}
