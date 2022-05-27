package com.itheima.stock.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysRolePermissionMapper;
import com.itheima.stock.mapper.SysUserRoleMapper;
import com.itheima.stock.ov.resp.LoginPermission;
import com.itheima.stock.ov.resp.PageResult;
import com.itheima.stock.ov.resp.R;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.pojo.SysRole;
import com.itheima.stock.pojo.SysRolePermission;
import com.itheima.stock.service.RoleService;
import com.itheima.stock.utils.IdWorker;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public R<PageResult> selectRole(HashMap<String, Integer> map) {
        Integer pageNum = map.get("pageNum");
        Integer pageSize = map.get("pageSize");

        PageHelper.startPage(pageNum, pageSize);

        List<SysRole> allRole = sysRoleMapper.findAllRole();

        if (CollectionUtils.isEmpty(allRole)) {
            return R.error("暂无数据");
        }

        PageInfo<SysRole> info = new PageInfo<>(allRole);

        PageResult<SysRole> result = new PageResult<>(info);

        return R.ok(result);
    }

    @Override
    public R<List<LoginPermission>> permissionsTree() {

        List<SysPermission> lists = sysPermissionMapper.findAllPermission();
        List<LoginPermission> menusBase = UserServiceImpl.getMenusBase(lists);

        return R.ok(menusBase);
    }

    @Override
    public R<String> addRole(HashMap<String, Object> map) {
        String name = (String) map.get("name");
        String description = (String) map.get("description");
        List<String> permissionsIds = (List) map.get("permissionsIds");

        HashSet<String> setIds = new HashSet<>();

        for (String permissionsId : permissionsIds) {
            setIds.add(permissionsId);
        }


        SysRole sysRole = new SysRole();
        sysRole.setName(name);
        sysRole.setDescription(description);
        sysRole.setStatus(1);
        sysRole.setCreateTime(DateTime.now().toDate());
        sysRole.setDeleted(1);
        long nextId = idWorker.nextId();
        sysRole.setId(String.valueOf(nextId));
        int i = sysRoleMapper.insertSelective(sysRole);

        SysRolePermission sysRolePermission = new SysRolePermission();
        sysRolePermission.setRoleId(String.valueOf(nextId));
        sysRolePermission.setCreateTime(DateTime.now().toDate());

        for (String setId : setIds) {
            sysRolePermission.setId(String.valueOf(idWorker.nextId()));
            sysRolePermission.setPermissionId(setId);
            sysRolePermissionMapper.insert(sysRolePermission);
        }

        return R.ok("操作成功");
    }

    @Override
    public R<List<String>> selectPermissionByRole(String roleId) {

        List<String> list = sysRolePermissionMapper.selectPermissionByRole(roleId);
        return R.ok(list);
    }

    @Override
    public R<String> insertRole(HashMap<String, Object> map) {
        String rid = (String) map.get("id");
        String name = (String) map.get("name");
        String description = (String) map.get("description");

        List<String> permissionsIds = (List) map.get("permissionsIds");
        HashSet<String> setIds = new HashSet<>();
        for (String permissionsId : permissionsIds) {
            setIds.add(permissionsId);
        }


        SysRole sysRole = new SysRole();
        sysRole.setId(rid);
        sysRole.setName(name);
        sysRole.setDescription(description);
        sysRole.setUpdateTime(DateTime.now().toDate());
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);

        sysRolePermissionMapper.deleteByRoleId(rid);

        SysRolePermission sysRolePermission = new SysRolePermission();
        sysRolePermission.setRoleId(rid);
        sysRolePermission.setCreateTime(DateTime.now().toDate());

        for (String setId : setIds) {
            sysRolePermission.setId(String.valueOf(idWorker.nextId()));
            sysRolePermission.setPermissionId(setId);
            sysRolePermissionMapper.insert(sysRolePermission);
        }

        return R.ok("操作成功");
    }

    @Override
    public R<String> deleteRole(String roleId) {

        sysUserRoleMapper.deleteByRoleId(roleId);
        sysRolePermissionMapper.deleteByRoleId(roleId);
        sysRoleMapper.deleteByRoleId(roleId);
        return R.ok("操作成功");
    }

    @Override
    public R<String> updateStatus(String roleId, Integer status) {

        sysRoleMapper.updateStatus(roleId,status);

        return R.ok("操作成功");
    }
}
