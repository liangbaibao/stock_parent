package com.itheima.stock.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.itheima.stock.common.enums.ResponseCode;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.mapper.SysUserRoleMapper;
import com.itheima.stock.ov.req.LoginReqVo;
import com.itheima.stock.ov.req.UpdateRoleReqVo;
import com.itheima.stock.ov.req.UserListReqVo;
import com.itheima.stock.ov.resp.*;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.pojo.SysRole;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.pojo.SysUserRole;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 定义服务接口实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 分布式环境保证生成id唯一
     */
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 生成验证码
     * map结构：
     * code： xxx,
     * rkey: xxx
     *
     * @return
     */
    @Override
    public R<Map> generateCaptcha() {
        //1.生成4位数字验证码
        String checkCode = RandomStringUtils.randomNumeric(4);
        //2.获取全局唯一id
        String rkey = String.valueOf(idWorker.nextId());
        //验证码存入redis中，并设置有效期1分钟
        redisTemplate.opsForValue().set(rkey, checkCode, 60, TimeUnit.SECONDS);
        //3.组装数据
        HashMap<String, String> map = new HashMap<>();
        map.put("rkey", rkey);
        map.put("code", checkCode);
        return R.ok(map);
    }

    @Override
    public R<PageResult> getUserList(UserListReqVo vo) {

//        UserListRespVo userListRespVo = new UserListRespVo();

        //1.设置分页参数
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        //2.通过mapper查询
        List<UserRespVo> userList = sysUserMapper.findAllUser();
        if (CollectionUtils.isEmpty(userList)) {
            return R.error("暂无数据");
        }
        PageInfo<UserRespVo> infos = new PageInfo<>(userList);

        //3.2 将PageInfo转PageResult
        PageResult<UserRespVo> pageResult = new PageResult<>(infos);
//        BeanUtils.copyProperties(pageResult, userListRespVo);
//        userListRespVo.setRows(userList);

        return R.ok(pageResult);
    }

    @Override
    public R<String> getUserAdd(SysUser vo) {

        //使用雪花算法添加id
        long uid = idWorker.nextId();
        vo.setId(String.valueOf(uid));

        //填入创建时间
        DateTime nowTime = DateTime.now();
        vo.setCreateTime(nowTime.toDate());

        sysUserMapper.insertSelective(vo);

        return R.ok("操作成功");

    }

    @Override
    public R<HashMap<String, Object>> findRole(String userId) {

        HashMap<String, Object> map = new HashMap<>();
        List<SysRole> allRole = sysRoleMapper.findAllRole();

        List<String> rids = sysUserRoleMapper.selectByUserId(userId);

        map.put("ownRoleIds", rids);
        map.put("allRole", allRole);

        return R.ok(map);
    }

    @Override
    public R<String> updateRole(UpdateRoleReqVo vo) {

        int i = sysUserRoleMapper.deleteByUserId(vo.getUserId());

        SysUserRole sysUserRole = new SysUserRole();
        for (String roleId : vo.getRoleIds()) {
            sysUserRole.setId(String.valueOf(idWorker.nextId()));
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreateTime(DateTime.now().toDate());
            sysUserRoleMapper.insertSelective(sysUserRole);
        }


        return R.ok("操作成功");
    }

    @Override
    public R<String> deleteUser(List<Long> userIds) {
        for (Long userId : userIds) {
            sysUserRoleMapper.deleteByUserId(String.valueOf(userId));
            sysUserMapper.deleteByPrimaryKey(userId);
        }

        return R.ok("操作成功");
    }

    @Override
    public R<SysUser> selectUserById(Long userId) {

        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);

        return R.ok(sysUser);
    }

    @Override
    public R<String> updateUser(SysUser sysUser) {

        int result = sysUserMapper.updateByPrimaryKeySelective(sysUser);

        return result > 0 ? R.ok("操作成功") : R.error("编辑失败");
    }


    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {

        if (vo == null || Strings.isNullOrEmpty(vo.getUsername())
                || Strings.isNullOrEmpty(vo.getPassword()) || Strings.isNullOrEmpty(vo.getRkey())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //验证码校验
        //获取redis中rkey对应的code验证码
        String rCode = (String) redisTemplate.opsForValue().get(vo.getRkey());

        //校验
        if (Strings.isNullOrEmpty(rCode) || !rCode.equals(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //redis清除key
        redisTemplate.delete(vo.getRkey());
        //根据用户名查询用户信息
        SysUser user = sysUserMapper.findByUserName(vo.getUsername());
        //判断用户是否存在，存在则密码校验比对
        if (user == null || !passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        //组装登录成功数据
        LoginRespVo respVo = new LoginRespVo();
        BeanUtils.copyProperties(user, respVo);

        //TODO：用户权限设计
        //权限树（包含按钮权限）

        //1.该用户所有权限信息
        List<SysPermission> lists = sysPermissionMapper.findPermissionByUserId(respVo.getId());

        List<LoginPermission> menusBase = getMenusBase(lists);

        respVo.setMenus(menusBase);


        //按钮权限集合
        HashSet<String> setPerms = new HashSet<>();
        for (SysPermission list : lists) {
            if (StringUtils.isNotBlank(list.getPerms())) {
                setPerms.add(list.getPerms());
            }
        }
        respVo.setPermissions(setPerms);


        return R.ok(respVo);
    }



    public static List<LoginPermission> getMenusBase(List<SysPermission> lists) {
        List<LoginPermission> menusBase = new ArrayList<>();
        for (SysPermission sysPermission : lists) {
            if ("0".equals(sysPermission.getPid())) {
                LoginPermission loginPermission = new LoginPermission();
                BeanUtils.copyProperties(sysPermission, loginPermission);
                loginPermission.setPath(sysPermission.getUrl());
                menusBase.add(loginPermission);
            }
        }

        for (LoginPermission loginPermission : menusBase) {
            List<LoginPermission> menus = iterateMenus(lists, loginPermission.getId());
            loginPermission.setChildren(menus);
        }
        return menusBase;
    }


    public static List<LoginPermission> iterateMenus(List<SysPermission> lists, String pid) {

        List<LoginPermission> result = new ArrayList<>();

        for (SysPermission list : lists) {
            String menuid = list.getId();//获取菜单的id
            String parentId = list.getPid();//获取菜单的父id

            if (StringUtils.isNotBlank(parentId)) {

                if (parentId.equals(pid)) {
                    //递归查询当前子菜单的子菜单
                    List<LoginPermission> iterateMenus = iterateMenus(lists, menuid);

                    LoginPermission menu = new LoginPermission();
                    BeanUtils.copyProperties(list, menu);
                    menu.setPath(list.getUrl());
                    menu.setChildren(iterateMenus);
                    result.add(menu);

                }
            }
        }
        return result;

    }


}