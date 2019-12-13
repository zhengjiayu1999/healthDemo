package com.itheima.health.jobs;


import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ClearOrderSetting {

    @Autowired
    OrderSettingDao orderSettingDao;

    public void clearOrder(){
        System.out.println("执行定时清理数据库任务");
        orderSettingDao.clearOrderSetting();
    }
}
