package self.auto_trading.data;

import com.alibaba.fastjson.JSONArray;

/*
 *DataUpdateMonitor.java
 *
 *Copyright(c) 2018 Chengdu Lanjing Data&Information Co., Ltd
 */
public interface DataUpdateMonitor {
    void update(JSONArray array);
}
