package com.main;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import java.util.GregorianCalendar;

import static org.quartz.DateBuilder.newDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @Author: cyb
 * @Date: 2019-02-12 09:37
 * @Version 1.0
 */
public class CalendarDemo {

    /**
     * HolidayCalendar。指定特定的日期，比如20140613。精度到天。
     * DailyCalendar。指定每天的时间段（rangeStartingTime, rangeEndingTime)，格式是HH:MM[:SS[:mmm]]。也就是最大精度可以到毫秒。
     * WeeklyCalendar。指定每星期的星期几，可选值比如为java.util.Calendar.SUNDAY。精度是天。
     * MonthlyCalendar。指定每月的几号。可选值为1-31。精度是天
     * AnnualCalendar。 指定每年的哪一天。使用方式如上例。精度是天。
     * CronCalendar。指定Cron表达式。精度取决于Cron表达式，也就是最大精度可以到秒。
     * @param args
     */
    public static void main(String[] args) {
        //创建scheduler
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            AnnualCalendar cal = new AnnualCalendar(); //定义一个每年执行Calendar，精度为天，即不能定义到2.25号下午2:00
            java.util.Calendar excludeDay = new GregorianCalendar();
            excludeDay.setTime(newDate().inMonthOnDay(2, 25).build());
            cal.setDayExcluded(excludeDay, true);  //设置排除2.25这个日期
            scheduler.addCalendar("FebCal", cal, false, false); //scheduler加入这个Calendar

            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1")
                    .startNow()//一旦加入scheduler，立即生效
                    .modifiedByCalendar("FebCal") //使用Calendar !!
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(1)
                            .repeatForever())
                    .build();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
