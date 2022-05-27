package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.*;
import com.itheima.stock.pojo.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author baibao
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 沪深两市个股涨幅分时行情数据查询，以时间顺序和涨幅查询前10条数据
     * @param curTime 当前股票有效时间
     * @return
     */
    List<StockUpdownDomain> stockIncreaseLimit(@Param("curTime") Date curTime);

    /**
     * 根据时间和涨幅降序排序全表查询
     * @return
     */
    List<StockUpdownDomain> stockAll();

    /**
     * 统计指定日期内涨停或者跌停的数据
     * 日期范围不能失效，否则分库分表查询失效
     * @param curTime 股票交易时间
     * @param openDate 对应的开盘日期
     * @param flag 1：涨停 0：跌停
     * @return
     */
    List<Map> upDownCount(@Param("avlDate") Date curTime, @Param("openDate") Date openDate, @Param("flag") Integer flag);

    /**
     * 统计指定时间点下，各个涨跌区间内股票的个数
     * @param avlDate
     * @return
     */
    List<Map> stockUpDownScopeCount(@Param("avlDate") Date avlDate);

    /**
     * 查询指定股票在指定日期下的每分钟的成交流水信息
     * @param code 股票编码
     * @param avlDate 最近的股票有效交易日期
     * @return
     */
    List<Stock4MinuteDomain> stockScreenTimeSharing(@Param("stockCode") String code, @Param("startDate") Date avlDate, @Param("endtDate") Date endDate);

    /**
     *  统计指定股票在指定日期范围内的每天交易数据统计
     * @param code 股票编码
     * @param beginDate 前推的日期时间
     * @return
     */
    List<Stock4EvrDayDomain> stockCreenDkLine(@Param("stockCode") String code, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);


    /**
     * 获取指定日期范围内的收盘价格
     * @param code 股票编码
     * @param beginDate 起始时间
     * @param endDate 结束时间
     * @return
     */
    List<Date> getCloseDates(@Param("code") String code, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 获取指定股票在指定日期下的数据
     * @param code 股票编码
     * @param dates 指定日期集合
     * @return
     */
    List<Stock4EvrDayDomain> getStockCreenDkLineData(@Param("code") String code, @Param("dates") List<Date> dates);

    /**
     * 批量插入功能
     * @param stockRtInfoList
     */
    int insertBatch(List<StockRtInfo> stockRtInfoList);

    List<Map> getLikeCode(@Param("searchStr") String searchStr);


    List<Stock4EvrWeekDomain> getstockWeekklineData(@Param("code") String code,@Param("dates") List<Date> closeDates);

    StockRtInfoDomain selectNewOneData(String code);

    List<StockFlowingWaterDomain> selectStockFlowingWater(String code);
}
