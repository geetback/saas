package cn.itcast.web.quartz;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class MyJob {

    public void printTime(){

        System.out.println("当前时间为:"+ DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }

}
