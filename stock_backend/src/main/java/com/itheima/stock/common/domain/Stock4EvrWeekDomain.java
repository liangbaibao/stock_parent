package com.itheima.stock.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author by itheima
 * @Date 2022/2/28
 * @Description 个股日K数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4EvrWeekDomain {


    /**
     * 一周内平均价
     */
   private BigDecimal avgPrice;
    /**
     * 一周内最低价
     */
    private BigDecimal minPrice;
    /**
     * 周一开盘价
     */
    private BigDecimal openPrice;
    /**
     * 一周内最高价
     */
    private BigDecimal maxPrice;
    /**
     * 周五收盘价（如果当前日期不到周五，则则显示最新cur_price）
     */
    private BigDecimal closePrice;

    /**
     * 一周内最大时间
     */
    private String mxTime;
    /**
     * 股票编码
     */
    private String stock_code;

}