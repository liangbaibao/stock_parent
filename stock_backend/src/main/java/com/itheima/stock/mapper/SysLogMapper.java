package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysLog;

import java.util.List;

/**
* @author baibao
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    List<SysLog> selectAll();


}
