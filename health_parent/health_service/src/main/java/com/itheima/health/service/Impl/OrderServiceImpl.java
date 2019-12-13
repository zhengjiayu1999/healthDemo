package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;
    private Date date;
    private Order order;

    @Override
    public Result order(Map map) {
        OrderSetting orderSetting;
        String orderDate = (String) map.get("orderDate");
        try {
            date = DateUtils.parseString2Date(orderDate);
            orderSetting = orderSettingDao.findByOrderDate(date);//查询是否存在此日期，存在可以预约，不存在则返回false
            if(orderSetting==null){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }else{
                int number=orderSetting.getNumber();
                int reservations=orderSetting.getReservations();
                if(reservations >= number){
                    //预约已满，不能预约
                    return new Result(false,MessageConstant.ORDER_FULL);
                }

                String telephone =(String) map.get("telephone");
                Member member = memberDao.findByTelephone(telephone);//通过手机号查询是否存在此会员
                if(member!=null){//说明有会员信息
                    Integer id = member.getId();
                    int setmealId=Integer.parseInt((String)map.get("setmealId"));
                    Order order=new Order(id,date,null,null,setmealId);
                    List<Order> list=orderDao.findByCondition(order);//通过三参数判断是否是同一人预约
                    if (list != null && list.size() > 0) {
                        //已经完成了预约，不能重复预约
                        return new Result(false, MessageConstant.HAS_ORDERED);
                    }
                    //通过以上判断，可以进行预约
                    orderSetting.setReservations(orderSetting.getReservations() + 1);
                    orderSettingDao.editReservationsByOrderDate(orderSetting);
                }else {
                    //不是会员添加到会员表
                    member = new Member();
                    member.setName((String) map.get("name"));
                    member.setPhoneNumber(telephone);
                    member.setIdCard((String) map.get("idCard"));
                    member.setSex((String) map.get("sex"));
                    member.setRegTime(new Date());
                    memberDao.add(member);
                }
                //会员添加成功，给订单表添加数据
                order = new Order(member.getId(),
                        date,
                        (String) map.get("orderType"),
                        Order.ORDERSTATUS_NO,
                        Integer.parseInt((String) map.get("setmealId")));
                orderDao.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }


    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById(id);
        if(map.get("orderDate")!=null){
            Date orderDate = (Date)map.get("orderDate");
            String date = null;
            try {
                date = DateUtils.parseDate2String(orderDate,"yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("orderDate",date);
        }
        return map;
    }



}
