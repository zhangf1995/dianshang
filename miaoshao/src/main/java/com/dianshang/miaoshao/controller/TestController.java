package com.dianshang.miaoshao.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianshang.common.en.StateCode;
import com.dianshang.common.resp.Result;
import com.dianshang.miaoshao.quartz.ScheduledJob;
import com.dianshang.miaoshao.quartz.SchedulerManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-11-07 17:10
 * @Description: 测试
 */
@RequestMapping("/miaoshaTest")
@RestController
public class TestController {

    @Autowired
    private SchedulerManager manager;

    @RequestMapping(value = "/testJob")
    public Result testJob(Long time,String info){
        try {
            String jsonStr = JSONObject.toJSONString(info);
            manager.startJob(jsonStr,new Date(time),"111","group", ScheduledJob.class);
        } catch (SchedulerException e) {
            return Result.me().response(StateCode.FAIL.getCode(),StateCode.FAIL.getMsg());
        }
        return Result.me().response(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMsg());
    }
}