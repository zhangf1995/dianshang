package com.dianshang.miaoshao.query;

import com.dianshang.common.entity.Page;
import lombok.Data;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-28 15:20
 * @Description:
 */
@Data
public class MiaoshaOrderQuery extends Page {
    private String miaoshaId;
    private String userId;
    private String productId;
}