package com.dianshang.miaoshao.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-11-07 14:32
 * @Description:
 */
@Component
@Slf4j
public class SchedulerManager {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private JobListener scheduleListener;

    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    public static final String STR_JSON = "strJson";

    /***
     *
     * @param date 时间
     * @return  cron类型的日期
     */
    public static String getCron(final Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 开始定时任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void startJob(String jsonStr,Date date,String jobName,String jobGroup,Class<? extends Job> jobClass) throws SchedulerException
    {
        String cron = getCron(date);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if(scheduleListener==null){
            scheduleListener=new SchedulerListener();
            scheduler.getListenerManager().addJobListener(scheduleListener);
        }
        JobKey jobKey=new JobKey(jobName,jobGroup);
        if(!scheduler.checkExists(jobKey))
        {
            scheduleJob(jsonStr,cron,scheduler,jobName,jobGroup,jobClass);
        }
    }

    /**
     * 移除定时任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void deleteJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey=new JobKey(jobName,jobGroup);
        scheduler.deleteJob(jobKey);
    }
    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void pauseJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey=new JobKey(jobName,jobGroup);
        scheduler.pauseJob(jobKey);
    }
    /**
     * 恢复定时任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void resumeJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey triggerKey=new JobKey(jobName,jobGroup);
        scheduler.resumeJob(triggerKey);
    }
    /**
     * 清空所有当前scheduler对象下的定时任务【目前只有全局一个scheduler对象】
     * @throws SchedulerException
     */
    public void clearAll() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.clear();
    }



    /**
     * 动态创建Job
     * 此处的任务可以配置可以放到properties或者是放到数据库中
     * Trigger:name和group 目前和job的name、group一致，之后可以扩展归类
     *
     * @param jsonStr
     * @param scheduler
     * @throws SchedulerException
     */
    private void scheduleJob(String jsonStr, String cron, Scheduler scheduler, String jobName, String jobGroup, Class<? extends Job> jobClass) throws SchedulerException{
        /*
         *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，更新任务的配置以及触发器
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中
         */
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
        cronTrigger.getJobDataMap().put(STR_JSON,jsonStr);
        scheduler.scheduleJob(jobDetail,cronTrigger);
        log.info("start job,info is {},jobName is {},jobGroup is {}",jsonStr,jobName,jobGroup);
    }
}