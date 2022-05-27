package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.StockBlockDomain;
import com.itheima.stock.pojo.StockBlockRtInfo;

import java.util.List;

/**
* @author baibao
* @description 针对表【stock_block_rt_info(股票板块详情信息表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.StockBlockRtInfo
*/
public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

    /**
     * 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    List<StockBlockDomain> sectorAllLimit();


    /**
     * 板块信息批量插入
     * @param list
     * @return
     */
    int insertBatch(List<StockBlockRtInfo> list);
}
