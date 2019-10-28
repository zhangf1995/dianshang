package com.dianshang.miaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("miaosha_product")
public class MiaoshaProduct {
    private String id;
    private String productId;
    private BigDecimal stockNum;
    private Date startTime;
    private Date endTime;
}