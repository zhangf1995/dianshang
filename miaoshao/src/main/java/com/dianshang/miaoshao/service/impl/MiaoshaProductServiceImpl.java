package com.dianshang.miaoshao.service.impl;

import com.dianshang.miaoshao.entity.MiaoshaOrder;
import com.dianshang.miaoshao.entity.MiaoshaProduct;
import com.dianshang.miaoshao.mapper.MiaoshaProductMapper;
import com.dianshang.miaoshao.query.MiaoshaOrderQuery;
import com.dianshang.miaoshao.query.MiaoshaProductQuery;
import com.dianshang.miaoshao.service.MiaoshaOrderService;
import com.dianshang.miaoshao.service.MiaoshaProductService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:48
 * @Description: impl
 */
@Service
@Slf4j
public class MiaoshaProductServiceImpl implements MiaoshaProductService {

    @Autowired
    private MiaoshaProductMapper mapper;
    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    //atomic原子类，成功情况下进行+1
    public static AtomicInteger successAtomic  = new AtomicInteger(0);

    public static AtomicInteger failAtomic = new AtomicInteger(0);

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
            String userId = UUID.randomUUID().toString().replace("-", "");
            miaoshaOrderQuery.setUserId(userId);
            MiaoshaOrder miaoshaOrder = miaoshaOrderService.queryByCondition(miaoshaOrderQuery);
            if(null == miaoshaOrder){
                //新增秒杀数据,后期增加feign，新增订单数据,使用lcn实现分布式事务
                try{
                    MiaoshaOrder newMiaoshaOrder = new MiaoshaOrder();
                    newMiaoshaOrder.setMiaoshaId(miaoshaProduct.getId());
                    newMiaoshaOrder.setProductId(miaoshaProduct.getProductId());
                    newMiaoshaOrder.setUserId(userId);
                    newMiaoshaOrder.setCreateTime(new Date());
                    miaoshaOrderService.save(newMiaoshaOrder);
                    successAtomic.incrementAndGet();
                    log.info("successAtomic is {}",successAtomic.get());
                }catch (Exception e){
                    log.info("miaosha save is error,{}",e.getMessage());
                    throw new Exception("服务器开了会小差~,请稍后再试");
                }
            }else{
                //后期可能会自定义exception
                throw new Exception("您已经秒杀过该商品");
            }
        }else{
            failAtomic.incrementAndGet();
            log.info("failAtomic is {}",failAtomic.get());
            throw new Exception("您的手慢了，该秒杀商品已卖完");
        }
    }
}