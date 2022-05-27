package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* @author baibao
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2022-05-20 18:45:54
* @Entity com.itheima.stock.pojo.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> findAllRole();

    void deleteByRoleId(String roleId);

    void updateStatus(@Param("roleId") String roleId,@Param("status") Integer status);
}
