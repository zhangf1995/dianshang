package com.dianshang.miaoshao.service;


import com.dianshang.miaoshao.entity.MiaoshaProduct;

public interface MiaoshaProductService {
    /**
     * 秒杀整体流程是否成功
     * @param miaoshaProduct
     * @return
     */
    //void miaoshaStart(MiaoshaProduct miaoshaProduct) throws Exception;

    void syncMiaoShaStart(MiaoshaProduct miaoshaProduct) throws Exception;

    /**
     * 新增秒杀数据
     * @param miaoshaProduct
     */
    void addMiaoshaProduct(MiaoshaProduct miaoshaProduct) throws Exception;
}
