package com.dianshang.miaoshao.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.ObjectUtils;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-11-07 14:30
 * @Description:
 */
public class ScheduledJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getTrigger().getJobDataMap();
        Object value = map.get(SchedulerManager.STR_JSON);
        if(!ObjectUtils.isEmpty(value)){
            String valueStr = String.valueOf(value);
            System.out.println(valueStr);
        }
    }
}