package com.dianshang.miaoshao.service;

import com.dianshang.miaoshao.entity.MiaoshaOrder;
import com.dianshang.miaoshao.query.MiaoshaOrderQuery;

public interface MiaoshaOrderService {
    /**
     * 条件查询miaosha_order
     * @param miaoshaOrderQuery
     * @return
     */
    MiaoshaOrder queryByCondition(MiaoshaOrderQuery miaoshaOrderQuery);
}
