package com.itheima.stock.controller;

import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LogController {

    @Autowired
    private LogService logService;


    @PostMapping("/logs")
    public R<PageResult> findLog(@RequestBody UserListReqVo vo){

        R<PageResult> result = logService.findLog(vo);

        return result;

    }

    @DeleteMapping("/log")
    public R<String> deleteLog(@RequestBody List<Long> logIds) {
        R<String> result = logService.deleteLog(logIds);

        return result;
    }



}
