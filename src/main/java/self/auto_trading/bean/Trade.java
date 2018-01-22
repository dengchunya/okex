package self.auto_trading.bean;

import java.time.LocalDateTime;

/*
 *Trade.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public class Trade {
    private double price;
    private double size;
    private int side;
    private Integer id;

    public Trade(){}

    public Trade(double price, double size, Integer side) {
        this.price = price;
        this.size = size;
        this.side = side;
    }

    public Trade(int id, double price, double size, Integer side) {
        this.id = id;
        this.price = price;
        this.size = size;
        this.side = side;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
