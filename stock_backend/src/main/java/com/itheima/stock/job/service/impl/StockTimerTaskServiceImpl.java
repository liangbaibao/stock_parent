package com.itheima.stock.job.service.impl;

import com.itheima.stock.common.domain.StockInfoConfig;
import com.itheima.stock.job.service.StockTimerTaskService;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("stockTimerTaskService")
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Override
    public void getInnerMarketInfo() {
        //组装动态url
        //http://hq.sinajs.cn/list=s_sh000001,s_sz399001
        String innerUrl=stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
        //设置请求头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","http://finance.sina.com.cn");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        //发起请求
        String result = restTemplate.postForObject(innerUrl, new HttpEntity<>(headers), String.class);
//        String result = restTemplate.getForObject(innerUrl, String.class);
        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(result);
        //收集大盘封装后后的对象
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()) {
            //获取大盘的id
            String marketCode = matcher.group(1);
            System.out.println(marketCode);
            //其它信息
            String other = matcher.group(2);
            String[] others = other.split(",");
            //大盘名称
            String marketName=others[0];
            //当前点
            BigDecimal curPoint=new BigDecimal(others[1]);
            //当前价格
            BigDecimal curPrice=new BigDecimal(others[2]);
            //涨跌率
            BigDecimal upDownRate=new BigDecimal(others[3]);
            //成交量
            Long tradeAmount=Long.valueOf(others[4]);
            //成交金额
            Long tradeVol=Long.valueOf(others[5]);
            //当前日期
            Date now = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()).toDate();
            //封装对象
            StockMarketIndexInfo stockMarketIndexInfo = StockMarketIndexInfo.builder().id(idWorker.nextId()+"")
                    .markName(marketName)
                    .tradeVolume(tradeVol)
                    .tradeAccount(tradeAmount)
                    .updownRate(upDownRate)
                    .curTime(now)
                    .curPoint(curPoint)
                    .currentPrice(curPrice)
                    .markId(marketCode)
                    .build();
            list.add(stockMarketIndexInfo);
        };
        log.info("集合长度：{}，内容：{}", list.size(), list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            log.info("");
            return;
        }
        String curTime = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        log.info("采集的大盘数据：{},当前时间：{}", list,curTime);
        //TODO 后续完成批量插入功能
        //批量插入
        int count = this.stockMarketIndexInfoMapper.insertBatch(list);
        log.info("批量插入了：{}条数据",count);
    }

    @Override
    public void getOuterMarketInfo() {
        //组装动态url
        //http://hq.sinajs.cn/list=s_sh000001,s_sz399001
        String innerUrl=stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getOuter());
        //设置请求头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer","http://finance.sina.com.cn");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        //发起请求
        String result = restTemplate.postForObject(innerUrl, new HttpEntity<>(headers), String.class);
//        String result = restTemplate.getForObject(innerUrl, String.class);
        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(result);
        //收集大盘封装后后的对象
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()) {
            //获取大盘的id
            String marketCode = matcher.group(1);
            System.out.println(marketCode);
            //其它信息
            String other = matcher.group(2);
            String[] others = other.split(",");

            //大盘名称
            String marketName=others[0];
            //当前点
            BigDecimal curPoint=new BigDecimal(others[1]);
            //涨跌点
            BigDecimal curPrice=new BigDecimal(others[2]);
            //涨跌幅
            BigDecimal upDownRate=new BigDecimal(others[3]);
            //成交量
//            Long tradeAmount=Long.valueOf(others[4]);
            Long tradeAmount=null;
            //成交金额
//            Long tradeVol=Long.valueOf(others[5]);
            Long tradeVol=null;
            //当前日期
            Date now = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()).toDate();
            //封装对象
            StockMarketIndexInfo stockMarketIndexInfo = StockMarketIndexInfo.builder().id(idWorker.nextId()+"")
                    .markName(marketName)
                    .tradeVolume(tradeVol)
                    .tradeAccount(tradeAmount)
                    .updownRate(upDownRate)
                    .curTime(now)
                    .curPoint(curPoint)
                    .currentPrice(curPrice)
                    .markId(marketCode)
                    .build();
            list.add(stockMarketIndexInfo);
        };
        log.info("集合长度：{}，内容：{}", list.size(), list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            log.info("");
            return;
        }
        String curTime = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        log.info("采集的大盘数据：{},当前时间：{}", list,curTime);
        //TODO 后续完成批量插入功能
        //批量插入
        int count = this.stockMarketIndexInfoMapper.insertBatch(list);
        log.info("批量插入了：{}条数据",count);
    }

    @Override
    public void getStockRtIndex() {
        List<String> stockIds = stockBusinessMapper.getStockIds();
        System.out.println(stockIds.size());
    }
} 