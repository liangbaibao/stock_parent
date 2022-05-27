package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysUserRole;

import java.util.List;

/**
* @author baibao
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    List<String> selectByUserId(String userId);


    int deleteByUserId(String userId);

    void deleteByRoleId(String roleId);
}
