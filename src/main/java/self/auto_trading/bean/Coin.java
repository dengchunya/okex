package self.auto_trading.bean;

/*
 *Coin.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class Coin {
    private String code;
    private double increment;//每次交易比第一个交易的人多(少)出的钱
    private float tradeNum;//交易数量

    public Coin(String code, double increment, float tradeNum) {
        this.code = code;
        this.increment = increment;
        this.tradeNum = tradeNum;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public float getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(float tradeNum) {
        this.tradeNum = tradeNum;
    }
}
