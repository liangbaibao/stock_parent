package com.itheima.stock.common.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by itheima
 * @Date 2022/1/9
 * @Description 定义封装多内大盘数据的实体类
 */
@Data
public class StockRtInfoDomain {
    /*
      jdbc:bigint--->java:long
     */

    /*
        jdbc:decimal --->java:BigDecimal
     */
    private Long tradeAmt;//最新交易量
    private BigDecimal preClosePrice;//前收盘价格
    private BigDecimal lowPrice;//最低价
    private BigDecimal highPrice;//最高价
    private BigDecimal openPrice;//开盘价
    private BigDecimal tradeVol;//交易金额
    private BigDecimal tradePrice;//当前价格
    private String curDate;//当前日期
}