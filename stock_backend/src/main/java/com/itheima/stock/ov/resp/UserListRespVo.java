package com.itheima.stock.ov.resp;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *多条件综合查询用户分页信息,响应数据
 */

@Data
public class UserListRespVo implements Serializable {

    private Integer totalRows;

    private Integer totalPages;

    private Integer pageNum;

    private Integer pageSize;

    private Integer size;

    private List<UserRespVo> rows;


}
