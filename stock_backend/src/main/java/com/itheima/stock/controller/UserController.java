package com.itheima.stock.controller;

import com.itheima.stock.ov.req.LoginReqVo;
import com.itheima.stock.ov.req.UpdateRoleReqVo;
import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.LoginRespVo;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.ov.resp.UserListRespVo;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author by itheima
 * @Date 2021/12/29
 * @Description 定义用户访问层
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 用户登录功能实现
     *
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {
        R<LoginRespVo> user = userService.login(vo);

        return user;
    }


    /**
     * 生成验证码
     * map结构：
     * code： xxx,
     * rkey: xxx
     *
     * @return
     */
    @GetMapping("/captcha")
    public R<Map> generateCaptcha() {
        return userService.generateCaptcha();
    }


    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     *
     * @param vo
     * @return
     */
    @PostMapping("/users")
    public R<PageResult> userList(@RequestBody UserListReqVo vo) {
        R<PageResult> userList = userService.getUserList(vo);

        return userList;
    }

    /**
     * 添加用户信息
     *
     * @param vo
     * @return
     */
    @PostMapping("/user")
    public R<String> userAdd(@RequestBody SysUser vo) {
        R<String> userAdd = userService.getUserAdd(vo);

        return userAdd;
    }

    @GetMapping("/user/roles/{userId}")
    public R<HashMap<String, Object>> selectRole(@PathVariable String userId) {

        R<HashMap<String, Object>> map = userService.findRole(userId);

        return map;
    }


    @PutMapping("/user/roles")
    public R<String> updateRole(@RequestBody UpdateRoleReqVo vo) {
        R<String> newRole = userService.updateRole(vo);

        return newRole;
    }


    @DeleteMapping("/user")
    public R<String> deleteUser(@RequestBody List<Long> userIds) {
        R<String> result = userService.deleteUser(userIds);

        return result;
    }


    @GetMapping("/user/info/{userId}")
    public R<SysUser> selectUserById(@PathVariable Long userId) {

        R<SysUser> result = userService.selectUserById(userId);

        return result;
    }

    @PutMapping("/user")
    public R<String> updateUser(@RequestBody SysUser sysUser) {

        R<String> result = userService.updateUser(sysUser);

        return result;
    }



//    @GetMapping("/test")
//    public String getName(){
//        return "itheima";
//    }
}