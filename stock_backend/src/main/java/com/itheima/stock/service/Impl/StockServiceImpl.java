package com.itheima.stock.service.Impl;

import ch.qos.logback.classic.Logger;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.stock.common.domain.*;
import com.itheima.stock.common.enums.ResponseCode;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.StockBusiness;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2021/12/19
 * @Description
 */
@Service("stockService")
@Log4j2
public class StockServiceImpl implements StockService {

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    //注入mapper接口
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;



    @Override
    public List<StockBusiness> getAllStockBusiness() {
        return stockBusinessMapper.getAll();
    }


    /**
     * 获取国内大盘的实时数据
     *
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> innerIndexAll() {
        //1.获取国内大盘的id集合
        List<String> innerIds = stockInfoConfig.getInner();
        //2.获取最近最新的股票有效交易日
        Date lDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock数据
//        String mockDate="20211226105600";//TODO后续大盘数据实时拉去，将该行注释掉 传入的日期秒必须为0
//        lDate = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //3.调用mapper查询指定日期下对应的国内大盘数据
        List<InnerMarketDomain> maps = stockMarketIndexInfoMapper.selectByIdsAndDate(innerIds, lDate);
        //组装响应的额数据
        if (CollectionUtils.isEmpty(maps)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(maps);
    }

    /**
     * 获取国外大盘的实时数据
     *
     * @return
     */
    @Override
    public R<List<OuterMarketDomain>> outerIndexAll() {
        //1.获取国外大盘的id集合
        List<String> outerIds = stockInfoConfig.getOuter();
        //2.获取最近最新的股票有效交易日
        Date lDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock数据
//        String mockDate="20211226105600";//TODO后续大盘数据实时拉去，将该行注释掉 传入的日期秒必须为0
//        lDate = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //3.调用mapper查询指定日期下对应的国内大盘数据
        List<OuterMarketDomain> maps = stockMarketIndexInfoMapper.selectByIdsAndDate1(outerIds, lDate);
        //组装响应的额数据
        if (CollectionUtils.isEmpty(maps)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(maps);
    }


    /**
     * 需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {
        //1.调用mapper接口获取数据 TODO 优化 避免全表查询 根据时间范围查询，提高查询效率
        List<StockBlockDomain> infos = stockBlockRtInfoMapper.sectorAllLimit();

        //2.组装数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }

    /**
     * 沪深两市个股涨幅分时行情数据查询，以时间顺序和涨幅查询前10条数据
     *
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> stockIncreaseLimit() {
        //1.直接调用mapper查询前10的数据 TODO 以时间顺序取前10
        //优化：
        //获取当前最近有效时间
        Date curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock数据
//        String mockStr="2021-12-27 09:47:00";
//        curDateTime=DateTime.parse(mockStr,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();


        List<StockUpdownDomain> infos = stockRtInfoMapper.stockIncreaseLimit(curDateTime);

        //2.判断是否有数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }


    /**
     * 沪深两市个股行情列表查询 ,以时间顺序和涨幅分页查询
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> stockPage(Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);
        //2.通过mapper查询
        List<StockUpdownDomain> infos = stockRtInfoMapper.stockAll();
        if (CollectionUtils.isEmpty(infos)) {
            return R.error("暂无数据");
        }
        //3.封装到PageResult下
        //3.1 封装PageInfo对象
        PageInfo<StockUpdownDomain> listPageInfo = new PageInfo<StockUpdownDomain>(infos);
        //3.2 将PageInfo转PageResult
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(listPageInfo);
        //4.封装R响应对象
        return R.ok(pageResult);
    }

    /**
     * 功能描述：沪深两市涨跌停分时行情数据查询，查询T日每分钟的涨跌停数据（T：当前股票交易日）
     * 查询每分钟的涨停和跌停的数据的同级；
     * 如果不在股票的交易日内，那么就统计最近的股票交易下的数据
     * map:
     * upList:涨停数据统计
     * downList:跌停数据统计
     *
     * @return
     */
    @Override
    public R<Map> upDownCount() {
        //1.获取股票最近的有效交易日期,精确到秒
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前最近有效期
        Date curTime = curDateTime.toDate();
        //开盘日期
        Date openTime = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //TODO mock_data 后续数据实时获取时，注释掉
//        String curTimeStr="20220106142500";
        //对应开盘日期 mock_data
//        String openTimeStr="20220106092500";
//        curTime = DateTime.parse(curTimeStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
//        openTime = DateTime.parse(openTimeStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //2.统计涨停的数据 约定：1代表涨停 0：跌停
        List<Map> upCount = stockRtInfoMapper.upDownCount(curTime, openTime, 1);
        //3.统计跌停的数据
        List<Map> downCount = stockRtInfoMapper.upDownCount(curTime, openTime, 0);
        //4.组装数据到map
        HashMap<String, List<Map>> info = new HashMap<>();
        info.put("upList", upCount);
        info.put("downList", downCount);
        //5.响应
        return R.ok(info);
    }


    /**
     * 将指定页的股票数据导出到excel表下
     *
     * @param response
     * @param page     当前页
     * @param pageSize 每页大小
     */
    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        try {
            //1.设置响应数据的类型:excel
            response.setContentType("application/vnd.ms-excel");
            //2.设置响应数据的编码格式
            response.setCharacterEncoding("utf-8");
            //3.设置默认的文件名称
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            //设置默认文件名称
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.分页查询股票数据
            PageHelper.startPage(page, pageSize);
            List<StockUpdownDomain> infos = this.stockRtInfoMapper.stockAll();
            Gson gson = new Gson();
            List<StockExcelDomain> excelDomains = infos.stream().map(info -> {
                StockExcelDomain domain = new StockExcelDomain();
                BeanUtils.copyProperties(info, domain);
                return domain;
            }).collect(Collectors.toList());
            //5.导出
            EasyExcel.write(response.getOutputStream(), StockExcelDomain.class).sheet("股票数据").doWrite(excelDomains);
        } catch (IOException e) {
            log.info("股票excel数据导出异常，当前页：{}，每页大小：{}，异常信息：{}", page, pageSize, e.getMessage());
        }
    }


    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * map结构示例：
     * {
     * "volList": [{"count": 3926392,"time": "202112310930"},......],
     * "yesVolList":[{"count": 3926392,"time": "202112310930"},......]
     * }
     *
     * @return
     */
    @Override
    public R<Map> stockTradeVol4InnerMarket() {
        //1.获取最近的股票交易日时间，精确到分钟 T交易日
        DateTime tDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date tDateTime = tDate.toDate();
        //对应的开盘时间
        Date tOpenTime = DateTimeUtil.getOpenDate(tDate).toDate();
        //TODO 后续注释掉 mock-data
//        String tDateStr="20220103143000";
//        tDateTime = DateTime.parse(tDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //mock-data
//        String openDateStr="20220103093000";
//        tOpenTime = DateTime.parse(openDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //获取T-1交易日
        DateTime preTDate = DateTimeUtil.getPreviousTradingDay(tDate);
        Date preTTime = preTDate.toDate();
        Date preTOpenTime = DateTimeUtil.getOpenDate(preTDate).toDate();
        //TODO 后续注释掉 mock-data
//        String preTStr="20220102143000";
//        preTTime = DateTime.parse(preTStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //mock-data
//        String openDateStr2="20220102093000";
//        preTOpenTime= DateTime.parse(openDateStr2, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //2.获取T日的股票大盘交易量统计数据
        List<Map> tData = stockMarketIndexInfoMapper.stockTradeVolCount(stockInfoConfig.getInner(), tOpenTime, tDateTime);
        //3.获取T-1的数据
        List<Map> preTData = stockMarketIndexInfoMapper.stockTradeVolCount(stockInfoConfig.getInner(), preTOpenTime, preTTime);
        //4.组装数据
        HashMap<String, List<Map>> data = new HashMap<>();
        data.put("volList", tData);
        data.put("yesVolList", preTData);
        return R.ok(data);
    }


    /**
     * 功能描述：统计在当前时间下（精确到分钟），股票在各个涨跌区间的数量
     *  如果当前不在股票有效时间内，则以最近的一个有效股票交易时间作为查询时间点；
     * @return
     *  响应数据格式：
     *  {
     *     "code": 1,
     *     "data": {
     *         "time": "2021-12-31 14:58:00",
     *         "infos": [
     *             {
     *                 "count": 17,
     *                 "title": "-3~0%"
     *             },
     *             //...
     *             ]
     *     }
     */
    /*@Override
    public R<Map> stockUpDownScopeCount() {
        //1.获取当前时间下最近的一个股票交易时间 精确到秒
        DateTime avlDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date avlDate = avlDateTime.toDate();
        //TODO 后续删除 mock-data
        String  mockDate="20220106095500";
        avlDate = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //2.查询
        List<Map> maps=stockRtInfoMapper.stockUpDownScopeCount(avlDate);
        //3.组装data
        HashMap<String, Object> data = new HashMap<>();
        data.put("time",avlDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",maps);
        //返回响应数据
        return R.ok(data);
    }*/

    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     *
     * @return
     */
    @Override
    public R<Map> stockUpDownScopeCount() {
        //1.获取当前时间下最近的一个股票交易时间 精确到秒
        DateTime avlDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date avlDate = avlDateTime.toDate();
        //TODO 后续删除 mock-data
//        String  mockDate="20220106095500";
//        avlDate = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //2.查询
        List<Map> maps = stockRtInfoMapper.stockUpDownScopeCount(avlDate);
        //获取去股票涨幅区间的集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //将list集合下的字符串映射成Map对象
        List<Map> orderMap = upDownRange.stream().map(key -> {
            Optional<Map> title = maps.stream().filter(map -> key.equals(map.get("title"))).findFirst();
            //判断对应的map是否存在
            Map tmp = null;
            if (title.isPresent()) {
                tmp = title.get();
            } else {
                tmp = new HashMap();
                tmp.put("title", key);
                tmp.put("count", 0);
            }
            return tmp;
        }).collect(Collectors.toList());
        //3.组装data
        HashMap<String, Object> data = new HashMap<>();
        data.put("time", avlDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos", orderMap);
        //返回响应数据
        return R.ok(data);
    }


    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code) {
        //1.获取最近有效的股票交易时间
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //获取当前日期
        Date curDate = curDateTime.toDate();
        //获取当前日期对应的开盘日期
        Date openDate = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //TODO 后续删除 mock-data
//        String mockDate="20220106142500";
//        curDate=DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
//        String openDateStr="20220106093000";
//        openDate=DateTime.parse(openDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        List<Stock4MinuteDomain> maps = stockRtInfoMapper.stockScreenTimeSharing(code, openDate, curDate);
        //响应前端
        return R.ok(maps);
    }


    /**
     * 功能描述：单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     * 		默认查询历史20天的数据；
     * @param code 股票编码
     * @return
     */
   /* @Override
    public R<List<Stock4EvrDayDomain>> stockCreenDkLine(String code) {
        //获取当前日期前推20天
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前时间节点
        Date curTime = curDateTime.toDate();
        //前推20
        Date pre20Day = curDateTime.minusDays(20).toDate();

        //TODO 后续删除
        String avlDate="20220106142500";
        curTime=DateTime.parse(avlDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        String openDate="20220101093000";
        pre20Day=DateTime.parse(openDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        List<Stock4EvrDayDomain> infos = stockRtInfoMapper.stockCreenDkLine(code, pre20Day, curTime);
        return R.ok(infos);
    }*/

    /**
     * 功能描述：单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     * 默认查询历史20天的数据；
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> stockCreenDkLine(String code) {
        //获取当前日期前推20天
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前时间节点
        Date curTime = curDateTime.toDate();
        //前推20
        Date pre20Day = curDateTime.minusDays(3).toDate();

        //TODO 后续删除
//        String avlDate="20220106142500";
//        curTime=DateTime.parse(avlDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
//        String openDate="20220101093000";
//        pre20Day=DateTime.parse(openDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        // List<Stock4EvrDayDomain> infos = stockRtInfoMapper.stockCreenDkLine(code, pre20Day, curTime);
        //获取指定范围的收盘日期集合
        List<Date> closeDates = stockRtInfoMapper.getCloseDates(code, pre20Day, curTime);
        //根据收盘日期精准查询，如果不在收盘日期，则查询最新数据
        List<Stock4EvrDayDomain> infos = stockRtInfoMapper.getStockCreenDkLineData(code, closeDates);
        return R.ok(infos);
    }

    /**
     * 模糊搜索
     * @param searchStr
     * @return
     */
    @Override
    public R<List<Map>> getLikeCode(String searchStr) {

        List<Map> maps = stockRtInfoMapper.getLikeCode(searchStr);


        //返回响应数据
        return R.ok(maps);
    }

    /**
     * 个股描述功能
     *
     * @param code
     * @return
     */
    @Override
    public R<Map> stockOneDescribe(String code) {

        HashMap<String, Object> map = stockBusinessMapper.stockOneDescribe(code);

        //返回响应数据
        return R.ok(map);
    }

    /**
     * 周K线
     * @param code
     * @return
     */
    @Override
    public R<List<Stock4EvrWeekDomain>> stockWeekkline(String code) {
        //获取当前日期前推20天
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前时间节点
        Date curTime = curDateTime.toDate();
        //前推20
        Date pre5Week = curDateTime.minusDays(5 * 7).toDate();

        //TODO 后续删除
//        String avlDate="20220106142500";
//        curTime=DateTime.parse(avlDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
//        String openDate="20220101093000";
//        pre20Day=DateTime.parse(openDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        // List<Stock4EvrDayDomain> infos = stockRtInfoMapper.stockCreenDkLine(code, pre20Day, curTime);
        //获取指定范围的收盘日期集合
        List<Date> closeDates = stockRtInfoMapper.getCloseDates(code, pre5Week, curTime);
        //根据收盘日期精准查询，如果不在收盘日期，则查询最新数据
        List<Stock4EvrWeekDomain> infos = stockRtInfoMapper.getstockWeekklineData(code, closeDates);

        return R.ok(infos);

    }

    /**
     * 个股最新分时行情数据接口分析
     * @param code
     * @return
     */
    @Override
    public R<StockRtInfoDomain> stockNewOneData(String code) {

        StockRtInfoDomain list = stockRtInfoMapper.selectNewOneData(code);


        return R.ok(list);
    }

    @Override
    public R<List<StockFlowingWaterDomain>> stockFlowingWater(String code) {

        List<StockFlowingWaterDomain> list = stockRtInfoMapper.selectStockFlowingWater(code);


        return R.ok(list);
    }
}