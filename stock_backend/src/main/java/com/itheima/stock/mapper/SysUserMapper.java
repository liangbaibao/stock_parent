package com.itheima.stock.mapper;

import com.itheima.stock.ov.resp.LoginPermission;
import com.itheima.stock.ov.resp.UserRespVo;
import com.itheima.stock.pojo.SysUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author baibao
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser findByUserName(String username);


    HashMap<Object, Object> findAllByUserName(String username);

    List<String> findRoleByUserId(String uid);


    List<LoginPermission> findPermissionByRoleId(String rid);

    List<UserRespVo> findAllUser();



}
