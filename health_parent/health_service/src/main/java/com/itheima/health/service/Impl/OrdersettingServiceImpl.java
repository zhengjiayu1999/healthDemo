package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao ordersettingDao;


    @Override
    public void add(List<OrderSetting> list) {
        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                //检查此数据（日期）是否存在
                String dateStr = getStringDate(orderSetting);
                long count = ordersettingDao.findCountByOrderDate(dateStr);
                if(count > 0){
                    //已经存在，执行更新操作
                    ordersettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //不存在，执行添加操作
                    ordersettingDao.add(orderSetting);
                }
            }
        }
    }

    private String getStringDate(OrderSetting orderSetting) {
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        return format.format(orderSetting.getOrderDate());
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin = date + "-1";//2019-3-1
        String dateEnd = date + "-31";//2019-3-31
        List<OrderSetting> list=ordersettingDao.getOrderSettingByMonth(dateBegin,dateEnd);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String,Object> map=new HashMap<>();
            //date
            //number
            //reservations
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            data.add(map);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        String dateStr = getStringDate(orderSetting);
        long count = ordersettingDao.findCountByOrderDate(dateStr);
        if(count>0){
            ordersettingDao.editNumberByOrderDate(orderSetting);
        }else{
            ordersettingDao.add(orderSetting);
        }
    }
}
