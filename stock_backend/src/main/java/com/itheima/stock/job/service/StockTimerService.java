package com.itheima.stock.job.service;

public interface StockTimerService {


    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtIndex();

    /**
     * 获取板块数据
     */
    void getStockSectorRtIndex();
}
