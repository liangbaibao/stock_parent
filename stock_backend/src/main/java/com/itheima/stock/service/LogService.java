package com.itheima.stock.service;


import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.SysLog;

import java.util.List;

public interface LogService {

    R<PageResult> findLog(UserListReqVo vo);

    R<String> deleteLog(List<Long> logIds);
}
