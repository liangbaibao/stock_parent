package com.itheima.stock.service;


import com.itheima.stock.ov.resp.LoginPermission;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;

import java.util.HashMap;
import java.util.List;

public interface RoleService {

    R<PageResult> selectRole(HashMap<String,Integer> map);

    R<List<LoginPermission>> permissionsTree();


    R<String> addRole(HashMap<String, Object> map);


    R<List<String>> selectPermissionByRole(String roleId);


    R<String> insertRole(HashMap<String, Object> map);

    R<String> deleteRole(String roleId);


    R<String> updateStatus(String roleId, Integer status);
}
