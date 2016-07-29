package com.mulechina.job;

public class TriggerBean {

    /**
     * 标识
     */
    private String key;
    /**
     * 所属组
     */
    private String group;
    /**
     * 描述
     */
    private String description;
    /**
     * 启动时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 日历名称
     */
    private String calendarName;
    /**
     * 失火指令（参数0,1,2）
     * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY = -1
     * MISFIRE_INSTRUCTION_SMART_POLICY = 0 (默认)
     * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW = 1
     * MISFIRE_INSTRUCTION_DO_NOTHING = 2
     */
    private Integer misfireInstruction;
    /**
     * 任务代理类
     */
    private JobDetailProxyBean jobDetail;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    public JobDetailProxyBean getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetailProxyBean jobDetail) {
        this.jobDetail = jobDetail;
    }
}