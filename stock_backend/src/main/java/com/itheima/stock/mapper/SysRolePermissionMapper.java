package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysRolePermission;

import java.util.List;

/**
* @author baibao
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    List<String> selectPermissionByRole(String roleId);


    void deleteByRoleId(String rid);
}
