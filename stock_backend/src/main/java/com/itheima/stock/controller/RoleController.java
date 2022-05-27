package com.itheima.stock.controller;


import com.itheima.stock.ov.resp.LoginPermission;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PostMapping("/roles")
    public R<PageResult> selectRole(@RequestBody HashMap<String, Integer> map) {

        R<PageResult> result = roleService.selectRole(map);

        return result;
    }

    @GetMapping("/permissions/tree/all")
    public R<List<LoginPermission>> permissionsTree() {
        R<List<LoginPermission>> permissionsTree = roleService.permissionsTree();
        return permissionsTree;
    }

    @PostMapping("/role")
    public R<String> addRole(@RequestBody HashMap<String, Object> map) {

        R<String> result = roleService.addRole(map);

        return result;
    }


    @GetMapping("/role/{roleId}")
    public R<List<String>> selectPermissionByRole(@PathVariable String roleId) {
        R<List<String>> result = roleService.selectPermissionByRole(roleId);
        return result;
    }


    @PutMapping("/role")
    public R<String> insterRole(@RequestBody HashMap<String, Object> map) {
        R<String> result = roleService.insertRole(map);
        return result;
    }

    @DeleteMapping("/role/{roleId}")
    public R<String> deleteRole(@PathVariable String roleId) {
        R<String> result = roleService.deleteRole(roleId);
        return result;
    }


    /**
     * 更新用户的状态信息
     * @param
     * @return
     */
    @PostMapping("/role/{roleId}/{status}")
    public R<String> updateStatus(@PathVariable String roleId,@PathVariable Integer status) {

        R<String> result = roleService.updateStatus(roleId,status);

        return result;
    }





}
