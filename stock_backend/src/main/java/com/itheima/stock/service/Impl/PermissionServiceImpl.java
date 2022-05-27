package com.itheima.stock.service.Impl;


import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.PermissionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    private SysPermissionMapper sysPermissionMapper;



    @Override
    public R<List<SysPermission>> findPermission() {

        List<SysPermission> allPermission = sysPermissionMapper.findAllPermission();


        return R.ok(allPermission);
    }

    @Override
    public R<List<SysPermission>> findTree() {

        //TODO  添加权限时回显权限树,仅仅显示目录和菜单

        return null;
    }

    @Override
    public R<List<SysPermission>> addPermission() {
        //TODO 权限添加按钮

        return null;
    }

    @Override
    public R<String> updatePermission(SysPermission sysPermission) {

        sysPermission.setUpdateTime(DateTime.now().toDate());

        int i = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);

        return i>0 ? R.ok("添加成功"):R.error("添加失败");
    }

    @Override
    public R<String> deletePermission(String permissionId) {

        int result = sysPermissionMapper.updateDeleted(permissionId);

        return R.ok("删除成功");
    }
}
