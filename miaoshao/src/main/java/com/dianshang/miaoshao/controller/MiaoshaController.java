package com.dianshang.miaoshao.controller;

import com.dianshang.miaoshao.entity.MiaoshaProduct;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:29
 * @Description: 秒杀controller
 */
@RequestMapping("/miaosha")
public class MiaoshaController {

    @RequestMapping(value = "/addMiaosha", method = RequestMethod.POST)
    public void addMiaosha(@RequestBody MiaoshaProduct miaoshaProduct) {
        if (!ObjectUtils.isEmpty(miaoshaProduct)) {
            String userId = miaoshaProduct.getUserId();
            String productId = miaoshaProduct.getProductId();
            if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(productId)) {
                
            } else {

            }
        } else {

        }
    }
}