package com.itheima.stock.common.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by itheima
 * @Date 2022/1/9
 * @Description 定义封装多内大盘数据的实体类
 */
@Data
public class StockFlowingWaterDomain {
    /*
      jdbc:bigint--->java:long
     */

    /*
        jdbc:decimal --->java:BigDecimal
     */
    private String date;//当前时间，精确到分
    private Long tradeAmt;//最新交易量
    private BigDecimal tradeVol;//交易金额
    private BigDecimal tradePrice;//当前价格
}