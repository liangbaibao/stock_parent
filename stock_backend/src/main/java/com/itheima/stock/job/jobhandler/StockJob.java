package com.itheima.stock.job.jobhandler;

import com.itheima.stock.job.service.StockTimerService;
import com.itheima.stock.job.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义股票相关数据的定时任务
 * @author laofang
 */
@Component
public class StockJob {
    private static Logger logger = LoggerFactory.getLogger(StockJob.class);

    @XxlJob("hema_job_test")
    public void jobTest(){
        System.out.println("jobTest run.....");
    }

    public void init(){
        logger.info("init");
    }

    public void destroy(){
        logger.info("destory");
    }

    /**
     * 注入股票定时任务服务bean
     */
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Autowired
    private StockTimerService stockTimerService;


    /**
     * 定义定时任务，采集国内大盘数据
     */
    @XxlJob("getStockInnerMarketInfos")
    public void getStockInnerMarketInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定义定时任务，采集国外大盘数据
     */
    @XxlJob("getStockOuterMarketInfos")
    public void getStockOuterMarketInfos(){
        stockTimerTaskService.getOuterMarketInfo();
    }

    /**
     * 定时采集A股数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos(){
        stockTimerTaskService.getStockRtIndex();
    }


    /**
     * 板块定时任务
     */
    @XxlJob("getStockBlockInfoTask")
    public void getStockBlockInfoTask(){
        stockTimerService.getStockSectorRtIndex();
    }


    /**
     * 定义获取分钟级股票数据
     */
    @XxlJob("getStockRtIndex")
    public void getStockRtIndex(){
        stockTimerService.getStockRtIndex();
    }
}