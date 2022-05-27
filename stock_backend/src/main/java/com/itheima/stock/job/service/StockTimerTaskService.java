package com.itheima.stock.job.service;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description 定义采集股票数据的定时任务的服务接口
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 获取国外大盘的实时数据信息
     */
    void getOuterMarketInfo();

    void getStockRtIndex();
} 