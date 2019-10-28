package com.dianshang.miaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-28 15:13
 * @Description:
 */
@Data
@TableName("miaosha_order")
public class MiaoshaOrder {
    private String orderId;
    private String productId;
    private String userId;
    private String miaoshaId;
    private Date createTime;
    private Byte status;
}