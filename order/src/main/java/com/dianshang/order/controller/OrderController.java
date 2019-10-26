package com.dianshang.order.controller;

import com.dianshang.order.feignclient.MiaoshaFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:16
 * @Description: 下单接口
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private MiaoshaFeignClient miaoshaFeignClient;

    @RequestMapping(value = "/addOrder",method = RequestMethod.POST)
    public void addOrder(){

    }
}