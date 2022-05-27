package com.itheima.stock.ov.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/24
 * @Description 登录后响应前端的vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVo {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 电话
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实名称
     */
    private String realName;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 账户状态，1正常，2锁定
     */
    private Integer status;

    /**
     * 邮件
     */
    private String email;

    /**
     * 权限树（包含按钮权限）
     */
    private List<LoginPermission> menus;

    /**
     * 按钮权限集合
     */
    private HashSet<String> permissions;


}