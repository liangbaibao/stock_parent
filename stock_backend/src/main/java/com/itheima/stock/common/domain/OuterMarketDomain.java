package com.itheima.stock.common.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by itheima
 * @Date 2022/1/9
 * @Description 定义封装国外大盘数据的实体类
 */
@Data
public class OuterMarketDomain {
    /*
    jdbc:bigint--->java:long
    jdbc:decimal --->java:BigDecimal
    */

    private String name;//名称 marketName
    private BigDecimal curPoint;//当前点 curPoint
    private BigDecimal upDownPoint;//涨跌点 curPrice
    private BigDecimal upDownRate;//涨跌幅 upDownRate
    private String curTime;//时间

}