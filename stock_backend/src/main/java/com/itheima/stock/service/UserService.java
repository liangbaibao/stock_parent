package com.itheima.stock.service;

import com.itheima.stock.ov.req.LoginReqVo;
import com.itheima.stock.ov.req.UpdateRoleReqVo;
import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.LoginRespVo;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.ov.resp.UserListRespVo;
import com.itheima.stock.pojo.SysUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 用户服务
 */
public interface UserService {
    /**
     * 用户登录功能实现
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成验证码
     *  map结构：
     *      code： xxx,
     *      rkey: xxx
     * @return
     */
    R<Map> generateCaptcha();

    /**
     * 用户管理模块实现
     * @param vo
     * @return
     */
    R<PageResult> getUserList(UserListReqVo vo);

    R<String> getUserAdd(SysUser vo);

    R<HashMap<String, Object>> findRole(String userId);


    R<String> updateRole(UpdateRoleReqVo vo);

    R<String> deleteUser(List<Long> userIds);

    R<SysUser> selectUserById(Long userId);

    R<String> updateUser(SysUser sysUser);
}