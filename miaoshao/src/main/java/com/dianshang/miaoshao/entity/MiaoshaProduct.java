package com.dianshang.miaoshao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:37
 * @Description: 秒杀商品
 */
@Data
public class MiaoshaProduct {
    private String userId;
    private String productId;
    private BigDecimal stockNum;
    private Date startTime;
    private Date endTime;
}