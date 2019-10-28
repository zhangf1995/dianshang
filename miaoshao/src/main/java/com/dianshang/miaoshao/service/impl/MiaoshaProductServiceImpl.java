package com.dianshang.miaoshao.service.impl;

import com.dianshang.miaoshao.entity.MiaoshaOrder;
import com.dianshang.miaoshao.entity.MiaoshaProduct;
import com.dianshang.miaoshao.mapper.MiaoshaProductMapper;
import com.dianshang.miaoshao.query.MiaoshaOrderQuery;
import com.dianshang.miaoshao.query.MiaoshaProductQuery;
import com.dianshang.miaoshao.service.MiaoshaOrderService;
import com.dianshang.miaoshao.service.MiaoshaProductService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:48
 * @Description: impl
 */
@Service
public class MiaoshaProductServiceImpl implements MiaoshaProductService {

    @Autowired
    private MiaoshaProductMapper mapper;
    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    /**
     * 秒杀核心代码（无分布式锁,无缓存等）
     * @param miaoshaProduct
     * @return
     */
    @Override
    public void miaoshaStart(MiaoshaProduct miaoshaProduct) throws Exception{
        MiaoshaProductQuery query = new MiaoshaProductQuery();
        query.setId(miaoshaProduct.getId());
        MiaoshaProduct product = mapper.queryByCondition(query);
        //判断库存是否充足
        if(null != product && product.getStockNum().compareTo(BigDecimal.ZERO) > 0){
            //判断该用户是否秒杀过该商品
            MiaoshaOrderQuery miaoshaOrderQuery = new MiaoshaOrderQuery();
            miaoshaOrderQuery.setMiaoshaId(miaoshaProduct.getId());
            miaoshaOrderQuery.setProductId(product.getProductId());
            //暂时写死，后期通过sso实现分布式情况下用户信息的全局化
            miaoshaOrderQuery.setUserId("1");
            MiaoshaOrder miaoshaOrder = miaoshaOrderService.queryByCondition(miaoshaOrderQuery);
            if(null == miaoshaOrder){
                //新增秒杀数据,后期增加feign，新增订单数据
            }else{
                //后期可能会自定义exception
                throw new Exception("您已经秒杀过该商品");
            }
        }else{
            throw new Exception("您的手慢了，该秒杀商品已卖完");
        }
    }
}