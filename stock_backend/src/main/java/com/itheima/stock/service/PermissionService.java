package com.itheima.stock.service;

import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.SysPermission;

import java.util.List;

public interface PermissionService {


    R<List<SysPermission>> findPermission();

    R<List<SysPermission>> findTree();

    R<List<SysPermission>> addPermission();

    R<String> updatePermission(SysPermission sysPermission);


    R<String> deletePermission(String permissionId);

}
