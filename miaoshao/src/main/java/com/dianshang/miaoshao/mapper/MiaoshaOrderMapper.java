package com.dianshang.miaoshao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dianshang.miaoshao.entity.MiaoshaOrder;
import com.dianshang.miaoshao.query.MiaoshaOrderQuery;

public interface MiaoshaOrderMapper extends BaseMapper<MiaoshaOrder> {

    /**
     * 条件查询miaosha_order
     * @param miaoshaOrderQuery
     * @return
     */
    MiaoshaOrder queryByCondition(MiaoshaOrderQuery miaoshaOrderQuery);
}
