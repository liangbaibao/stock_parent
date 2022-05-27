package com.itheima.stock.controller;


import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @GetMapping("/permissions")
    public R<List<SysPermission>> findPermission(){
        R<List<SysPermission>> result = permissionService.findPermission();
        return result;
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @GetMapping("/permissions/tree")
    public R<List<SysPermission>> findTree(){
        R<List<SysPermission>> result = permissionService.findTree();
        return result;
    }


    /**
     * 权限添加按钮
     * @return
     */
    @GetMapping("/permission")
    public R<List<SysPermission>> addPermission(){
        R<List<SysPermission>> result = permissionService.addPermission();
        return result;
    }

    /**
     *  更新权限
     * @return
     */
    @PutMapping("/permission")
    public R<String> updatePermission(@RequestBody SysPermission sysPermission){
        R<String> result = permissionService.updatePermission(sysPermission);
        return result;
    }


    /**
     *  更新权限
     * @return
     */
    @DeleteMapping("/permission/{permissionId}")
    public R<String> deletePermission(@PathVariable String permissionId){
        R<String> result = permissionService.deletePermission(permissionId);
        return result;
    }




}
