package com.itheima.stock.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.SysLogMapper;
import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.ov.resp.UserRespVo;
import com.itheima.stock.pojo.SysLog;
import com.itheima.stock.service.LogService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogMapper sysLogMapper;



    @Override
    public R<PageResult> findLog(UserListReqVo vo) {

        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        //2.通过mapper查询
        List<SysLog> sysLogs = sysLogMapper.selectAll();
        if (CollectionUtils.isEmpty(sysLogs)) {
            return R.error("暂无数据");
        }
        PageInfo<SysLog> infos = new PageInfo<>(sysLogs);

        //3.2 将PageInfo转PageResult
        PageResult<SysLog> pageResult = new PageResult<>(infos);

        return R.ok(pageResult);

    }

    @Override
    public R<String> deleteLog(List<Long> logIds) {
        for (Long logId : logIds) {
            sysLogMapper.deleteByPrimaryKey(logId);
        }

        return R.ok("操作成功");
    }
}
