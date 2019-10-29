package com.dianshang.miaoshao.service.impl;

import com.dianshang.miaoshao.entity.MiaoshaOrder;
import com.dianshang.miaoshao.mapper.MiaoshaOrderMapper;
import com.dianshang.miaoshao.query.MiaoshaOrderQuery;
import com.dianshang.miaoshao.service.MiaoshaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-28 15:31
 * @Description:
 */
@Service
public class MiaoshaOrderServiceImpl implements MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderMapper mapper;
    /**
     * 条件查询miaosha_order
     * @param miaoshaOrderQuery
     * @return
     */
    @Override
    public MiaoshaOrder queryByCondition(MiaoshaOrderQuery miaoshaOrderQuery) {
        return mapper.queryByCondition(miaoshaOrderQuery);
    }

    /**
     * 保存秒杀订单数据
     * @param newMiaoshaOrder
     */
    @Override
    public void save(MiaoshaOrder newMiaoshaOrder) {
        mapper.insert(newMiaoshaOrder);
    }
}

