package self.auto_trading.bean;

import java.util.List;

/*
 *PriceList.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class PriceList {
    private List<Trade> sell;
    private List<Trade> buy;

    public PriceList(List<Trade> sell, List<Trade> buy) {
        this.sell = sell;
        this.buy = buy;
    }

    public List<Trade> getSell() {
        return sell;
    }

    public void setSell(List<Trade> sell) {
        this.sell = sell;
    }

    public List<Trade> getBuy() {
        return buy;
    }

    public void setBuy(List<Trade> buy) {
        this.buy = buy;
    }
}
