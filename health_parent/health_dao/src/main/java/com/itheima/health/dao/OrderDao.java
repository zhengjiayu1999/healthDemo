package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDao {

    List<Order> findByCondition(Order order);

    void add(Order order);


    Map findById(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountBetweenDate(Map<String, Object> weekMap);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(Map<String, Object> weekMap2);

    List<Map> findHotSetmeal();
}
