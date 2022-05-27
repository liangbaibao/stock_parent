package com.itheima.stock.ov.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginPermission {


    /**
     *权限ID
     */
    private String id;
    /**
     *权限标题
     */
    private String title;
    /**
     *权限图标（按钮权限无图片）
     */
    private String icon;
    /**
     *请求地址
     */
    private String path;
    /**
     *权限名称对应前端vue组件名称
     */
    private String name;

    /**
     *
     */
    private List<LoginPermission> children;




}
