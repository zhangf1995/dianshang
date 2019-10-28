package com.dianshang.miaoshao.service;


import com.dianshang.miaoshao.entity.MiaoshaProduct;

public interface MiaoshaProductService {
    /**
     * 秒杀整体流程是否成功
     * @param miaoshaProduct
     * @return
     */
    void miaoshaStart(MiaoshaProduct miaoshaProduct) throws Exception;
}
