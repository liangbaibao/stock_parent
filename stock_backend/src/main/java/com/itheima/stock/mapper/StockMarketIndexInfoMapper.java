package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.OuterMarketDomain;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author baibao
* @description 针对表【stock_market_index_info(股票大盘数据详情表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    //获取国内大盘
    List<InnerMarketDomain> selectByIdsAndDate(@Param("ids") List<String> ids, @Param("lastDate") Date lastDate);
    //获取国外大盘
    List<OuterMarketDomain> selectByIdsAndDate1(@Param("ids") List<String> ids, @Param("lastDate") Date lastDate);

    /**OuterMarketDomain
     * 查询指定大盘下的指定日期下小于等于指定时间的数据，结果包含：每分钟内，整体大盘的交易量的统计
     * @param marketIds 股票大盘的编码code集合
     * @param openDate 开盘时间
     * @param tStr 日期时间，精确到秒
     * @return
     */
    List<Map> stockTradeVolCount(@Param("marketIds") List<String> marketIds, @Param("openDate") Date openDate , @Param("stockDateTime") Date tStr);


    /**
     * 批量插入股票大盘数据
     * @param infos
     */
    int insertBatch(List<StockMarketIndexInfo> infos);
}
