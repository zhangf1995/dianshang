package com.dianshang.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-25 16:33
 * @Description:
 */
@Data
public class BaseEntity {
    private Date createTime;
    private Date updateTime;
}