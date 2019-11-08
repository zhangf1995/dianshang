package com.dianshang.miaoshao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianshang.common.utils.SnowFlake;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:48
 * @Description: impl
 */
@Service
@Slf4j
public class MiaoshaProductServiceImpl implements MiaoshaProductService {

    public static final String STOCK_NUM = "miaosha_stockNum_";

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Autowired
    private MiaoshaProductMapper mapper;
    @Autowired
    private MiaoshaOrderService miaoshaOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    //atomic原子类，成功情况下进行+1
    public static AtomicInteger successAtomic = new AtomicInteger(0);

    public static AtomicInteger failAtomic = new AtomicInteger(0);

    private final Lock lock = new ReentrantLock();

    /**
     * 秒杀核心代码（加锁）,很大情况下保证了数据的一致性，但依旧可能会出现线程安全问题（数据库事务提交慢）,通过redis来解决上述问题
     *
     * @param miaoshaProduct
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncMiaoShaStart(MiaoshaProduct miaoshaProduct) throws Exception {
        lock.lock();
        try {
            Object valueObj = redisTemplate.opsForValue().get(MiaoshaProductServiceImpl.STOCK_NUM + miaoshaProduct.getId());
            if (null == valueObj) {
                throw new Exception("该秒杀活动已结束或商品已被抢完");
            }
            MiaoshaProduct product = JSONObject.parseObject(String.valueOf(valueObj), MiaoshaProduct.class);
            //判断库存是否充足
            if (product.getStockNum().compareTo(BigDecimal.ZERO) > 0) {
                log.info("stockNum is {}", product.getStockNum());
                //分布式锁保证每个用户只能秒杀一次（暂时写死）
                String key = UUID.randomUUID().toString();
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, key, 10, TimeUnit.SECONDS);
                if (flag) {
                    //二次判断该用户是否秒杀过该商品
                    MiaoshaOrderQuery miaoshaOrderQuery = new MiaoshaOrderQuery();
                    miaoshaOrderQuery.setMiaoshaId(miaoshaProduct.getId());
                    miaoshaOrderQuery.setProductId(product.getProductId());
                    //暂时写死，后期通过sso实现分布式情况下用户信息的全局化
                    //String userId = UUID.randomUUID().toString().replace("-", "");
                    //测试分布式锁情况下，是否会出现同一个用户秒杀多次
                    String userId = "2";
                    miaoshaOrderQuery.setUserId(userId);
                    MiaoshaOrder miaoshaOrder = miaoshaOrderService.queryByCondition(miaoshaOrderQuery);
                    if (ObjectUtils.isEmpty(miaoshaOrder)) {
                        //新增秒杀数据,后期增加feign，新增订单数据,使用lcn实现分布式事务
                        try {
                            MiaoshaOrder newMiaoshaOrder = new MiaoshaOrder();
                            newMiaoshaOrder.setMiaoshaId(product.getId());
                            newMiaoshaOrder.setProductId(product.getProductId());
                            newMiaoshaOrder.setUserId(userId);
                            newMiaoshaOrder.setCreateTime(new Date());
                            miaoshaOrderService.save(newMiaoshaOrder);
                            //减库存,后期会用redis做缓存
                            BigDecimal newStockNum = product.getStockNum().subtract(BigDecimal.ONE);
                            product.setStockNum(newStockNum);
                            mapper.updateById(product);
                            product.setStockNum(newStockNum);
                            String productStr = JSONObject.toJSONString(product);
                            redisTemplate.opsForValue().set(STOCK_NUM + miaoshaProduct.getId(), productStr);
                            successAtomic.incrementAndGet();
                            log.info("successAtomic is {}", successAtomic.get());
                        } catch (Exception e) {
                            log.info("miaosha save is error,{}", e.getMessage());
                            throw new Exception("服务器开了会小差~,请稍后再试");
                        }
                    } else {//后期可能会自定义exception
                        throw new Exception("您已经秒杀过该商品");
                    }
                } else {
                    throw new Exception("您正在秒杀该商品，请勿重复");
                }
            } else {
                redisTemplate.delete(STOCK_NUM + miaoshaProduct.getId());
                failAtomic.incrementAndGet();
                log.info("failAtomic is {}", failAtomic.get());
                throw new Exception("您的手慢了，该秒杀商品已卖完");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 新增秒杀数据
     *
     * @param miaoshaProduct
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMiaoshaProduct(MiaoshaProduct miaoshaProduct) throws Exception {
        try {
            String id = String.valueOf(snowFlake.nextId());
            miaoshaProduct.setId(id);
            miaoshaProduct.setStartTime(new Date());
            miaoshaProduct.setEndTime(new Date());
            mapper.insert(miaoshaProduct);
            String productStr = JSONObject.toJSONString(miaoshaProduct);
            //暂时这么写，后期会在新增数据这块加个定时job,在job里面做redis缓存操作
            redisTemplate.opsForValue().set(MiaoshaProductServiceImpl.STOCK_NUM + miaoshaProduct.getId(), productStr);
        } catch (Exception e) {
            log.info("addMiaoshaProduct error ,{}", e.getMessage());
            throw new Exception("Server Interal Error");
        }
    }
}