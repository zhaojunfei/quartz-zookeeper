package com.mulechina.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;

public class SchedulerFactoryBean implements InitializingBean {

    protected static Logger logger = Logger.getLogger(SchedulerFactoryBean.class);
    /**
     * 触发器列表
     */
    private List<Object> triggers;
    /**
     * zooKeeper工厂
     */
    private ZookeeperFactory zooKeeperFactory;

    /**
     * Spring初始化方法
     * @throws SchedulerException
     */
    public void afterPropertiesSet() throws SchedulerException {
        this.initSchedulerFactory();
    }

    /**
     * 初始化调度器工厂
     * @throws SchedulerException
     */
    public void initSchedulerFactory() throws SchedulerException {
        //初始化StdSchedulerFactory
        StdSchedulerFactory schedulerFactory = SchedulerUtils.initStdSchedulerFactory();
        //获取调度器
        Scheduler scheduler = schedulerFactory.getScheduler();
        //装载调度器
        for(Object triggerObject : this.getTriggers()){
            if(triggerObject instanceof CronTriggerBean){
                CronTriggerBean cronTriggerBean = (CronTriggerBean)triggerObject;
                //获取任务代理类对象
                JobDetailProxyBean jobDetailProxyBean = cronTriggerBean.getJobDetail();
                //装配任务
                JobDetail jobDetail = SchedulerUtils.assemblyJobDetail(jobDetailProxyBean);
                //设置zooKeeper连接工厂
                jobDetail.getJobDataMap().put("zooKeeperFactory",this.getZooKeeperFactory());
                //装配触发器
                CronTrigger cronTrigger =  SchedulerUtils.assemblyCronTrigger(cronTriggerBean);
                scheduler.scheduleJob(jobDetail, cronTrigger);
//                System.out.println("CronTriggerBean");
            }else{
                SimpleTriggerBean simpleTriggerBean = (SimpleTriggerBean)triggerObject;
                //获取任务代理类对象
                JobDetailProxyBean jobDetailProxyBean = simpleTriggerBean.getJobDetail();
                //装配任务
                JobDetail jobDetail = SchedulerUtils.assemblyJobDetail(jobDetailProxyBean);
                //设置zooKeeper连接工厂
                jobDetail.getJobDataMap().put("zooKeeperFactory",this.getZooKeeperFactory());
                //装配触发器
                SimpleTrigger simpleTrigger =  SchedulerUtils.assemblySimpleTrigger(simpleTriggerBean);
                scheduler.scheduleJob(jobDetail, simpleTrigger);
//                System.out.println("SimpleTriggerBean");
            }
        }

        scheduler.start();
        logger.info("调度器已启动");
    }

    public List<Object> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Object> triggers) {
        this.triggers = triggers;
    }

    public ZookeeperFactory getZooKeeperFactory() {
        return zooKeeperFactory;
    }

    public void setZooKeeperFactory(ZookeeperFactory zooKeeperFactory) {
        this.zooKeeperFactory = zooKeeperFactory;
    }
}

 