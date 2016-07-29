package com.mulechina.job;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;

/**
 * Quartz调度工具类
 * Date: 14-5-15
 * Time: 下午6:10
 */
public class SchedulerUtils {

    protected static Logger logger = Logger.getLogger(SchedulerUtils.class);

    /**
     * 初始化StdSchedulerFactory
     * @return StdSchedulerFactory
     */
    public static StdSchedulerFactory initStdSchedulerFactory() {
        StdSchedulerFactory schedulerFactory = null;
        try{
            schedulerFactory = (StdSchedulerFactory) Class.forName(StdSchedulerFactory.class.getName()).newInstance();
            Properties mergedProps = new Properties();
            // 设置Quartz线程池设置
            mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
            mergedProps.setProperty("org.quartz.threadPool.threadCount", Integer.toString(10));

            schedulerFactory.initialize(mergedProps);
        } catch (Exception e){
            logger.error("初始化StdSchedulerFactory失败");
            logger.error(e);
        }
        return schedulerFactory;
    }

    /**
     * 装配任务
     * @param jobDetail 任务代理类
     * @return JobDetail
     */
    public static JobDetail assemblyJobDetail(JobDetailProxyBean jobDetail){

        JobBuilder jobBuilder = JobBuilder.newJob(jobDetail.getClass());
        //设置JobDetail身份标识与所属组
        String key = jobDetail.getKey();
        if(StringUtils.isNotBlank(key)){
            jobBuilder = jobBuilder.withIdentity(key, jobDetail.getGroup());
        }else{
            jobBuilder = jobBuilder.withIdentity(IdentityUtils.generatorUUID("JOB"), jobDetail.getGroup());
        }

        //设置任务描述
        if(StringUtils.isNotBlank(jobDetail.getDescription())){
            jobBuilder = jobBuilder.withDescription(jobDetail.getDescription());
        }

        //设置JobDetail数据参数
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("targetObject",jobDetail.getTargetObject());                   //目标对象
        jobDataMap.put("targetMethod",jobDetail.getTargetMethod());                   //目标方法
        jobDataMap.put("mode", jobDetail.getMode());                                  //运行模式
        jobBuilder = jobBuilder.usingJobData(jobDataMap);

        return jobBuilder.build();
    }

    /**
     * 装配表达式触发器
     * @param cronTriggerBean 表达式触发器
     * @return 表达式触发器
     */
    public static CronTrigger assemblyCronTrigger(CronTriggerBean cronTriggerBean){

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();

        //设置触发器身份标识与所属组
        String key = cronTriggerBean.getKey();
        if(StringUtils.isNotBlank(key)){
            triggerBuilder = triggerBuilder.withIdentity(key, cronTriggerBean.getGroup());
        }else{
            triggerBuilder = triggerBuilder.withIdentity(IdentityUtils.generatorUUID("CronTrigger"), cronTriggerBean.getGroup());
        }
        //设置描述
        if(StringUtils.isNotBlank(cronTriggerBean.getDescription())){
            triggerBuilder = triggerBuilder.withDescription(cronTriggerBean.getDescription());
        }
        //设置启动时间
        if(StringUtils.isNotBlank(cronTriggerBean.getStartTime())){
            triggerBuilder = triggerBuilder.startAt(DateUtil.string2Date(cronTriggerBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        }else{
            triggerBuilder = triggerBuilder.startNow();      //当启动时间为空默认立即启动调度器
        }
        //设置结束时间
        if(StringUtils.isNotBlank(cronTriggerBean.getEndTime())){
            triggerBuilder = triggerBuilder.endAt(DateUtil.string2Date(cronTriggerBean.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        //设置优先级
        if(cronTriggerBean.getPriority() != null){
            triggerBuilder = triggerBuilder.withPriority(cronTriggerBean.getPriority());
        }

        //设置Cron表达式(不允许为空)与集火指令
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronTriggerBean.getCronExpression());
        if(cronTriggerBean.getMisfireInstruction() != null){
            if(cronTriggerBean.getMisfireInstruction() == Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
                cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            }
            if(cronTriggerBean.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW) {
                cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            }
            if(cronTriggerBean.getMisfireInstruction() == CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING) {
                cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            }
        }
        triggerBuilder = triggerBuilder.withSchedule(cronScheduleBuilder);

        return (CronTrigger)triggerBuilder.build();
    }

    /**
     * 装配简单触发器
     * @param simpleTriggerBean 简单触发器
     * @return 简单触发器
     */
    public static SimpleTrigger assemblySimpleTrigger(SimpleTriggerBean simpleTriggerBean){

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();

        //设置触发器身份标识与所属组
        String key = simpleTriggerBean.getKey();
        if(StringUtils.isNotBlank(key)){
            triggerBuilder = triggerBuilder.withIdentity(key, simpleTriggerBean.getGroup());
        }else{
            triggerBuilder = triggerBuilder.withIdentity(IdentityUtils.generatorUUID("SimpleTrigger"), simpleTriggerBean.getGroup());
        }
        //设置描述
        if(StringUtils.isNotBlank(simpleTriggerBean.getDescription())){
            triggerBuilder = triggerBuilder.withDescription(simpleTriggerBean.getDescription());
        }
        //设置启动时间
        if(StringUtils.isNotBlank(simpleTriggerBean.getStartTime())){
            triggerBuilder = triggerBuilder.startAt(DateUtil.string2Date(simpleTriggerBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        }else{
            triggerBuilder = triggerBuilder.startNow();      //当启动时间为空默认立即启动调度器
        }
        //设置结束时间
        if(StringUtils.isNotBlank(simpleTriggerBean.getEndTime())){
            triggerBuilder = triggerBuilder.endAt(DateUtil.string2Date(simpleTriggerBean.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        //设置优先级
        if(simpleTriggerBean.getPriority() != null){
            triggerBuilder = triggerBuilder.withPriority(simpleTriggerBean.getPriority());
        }

        //设置简单触发器 时间间隔(不允许为空)、执行次数(默认为-1)与集火指令
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).withRepeatCount(-1);
        simpleScheduleBuilder = simpleScheduleBuilder.withIntervalInSeconds(simpleTriggerBean.getInterval());
        if(simpleTriggerBean.getRepeatCount() != null){
            simpleScheduleBuilder = simpleScheduleBuilder.withRepeatCount(simpleTriggerBean.getRepeatCount());
        }else{
            simpleScheduleBuilder = simpleScheduleBuilder.withRepeatCount(-1);
        }

        if(simpleTriggerBean.getMisfireInstruction() != null){
            if(simpleTriggerBean.getMisfireInstruction() == Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            }
            if(simpleTriggerBean.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
            }
            if(simpleTriggerBean.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
            }
            if(simpleTriggerBean.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
            }
            if(simpleTriggerBean.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionNowWithExistingCount();
            }
            if(simpleTriggerBean.getMisfireInstruction() == SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT) {
                simpleScheduleBuilder = simpleScheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
            }
        }
        triggerBuilder = triggerBuilder.withSchedule(simpleScheduleBuilder);

        return (SimpleTrigger)triggerBuilder.build();
    }
}