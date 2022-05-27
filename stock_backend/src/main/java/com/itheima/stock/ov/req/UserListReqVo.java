package com.itheima.stock.ov.req;

import lombok.Data;

import java.util.Date;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 登录请求vo
 */
@Data
public class UserListReqVo {

    /**
     * 查询页数
     */
    private Integer pageNum;
    /**
     * 每页查询的个数
     */
    private Integer pageSize;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 账户状态
     */
    private String status;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
}