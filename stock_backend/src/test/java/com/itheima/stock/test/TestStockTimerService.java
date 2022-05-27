package com.itheima.stock.test;

import com.itheima.stock.job.service.StockTimerService;
import com.itheima.stock.job.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author by itheima
 * @Date 2022/1/1
 * @Description
 */
@SpringBootTest
public class TestStockTimerService {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Autowired
    private StockTimerService stockTimerService;

    /**
     * 获取大盘数据
     */
    @Test
    public void test01(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 获取A股数据
     */
    @Test
    public void test02(){
        stockTimerTaskService.getStockRtIndex();
    }

    /**
     * 获取板块数据测试
     */
    @Test
    public void test03() throws InterruptedException {
        stockTimerService.getStockSectorRtIndex();
    }
}    