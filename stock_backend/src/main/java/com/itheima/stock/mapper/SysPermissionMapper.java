package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysPermission;

import java.util.List;

/**
* @author baibao
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> findPermissionByUserId(String id);


    List<SysPermission> findAllPermission();


    int updateDeleted(String permissionId);
}
