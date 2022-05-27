package com.itheima.stock.ov.req;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleReqVo {

    private String userId;

    private List<String> roleIds;
}
