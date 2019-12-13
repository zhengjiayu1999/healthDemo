package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.healthSetMealDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.healthSetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = healthSetMealService.class)
@Transactional
public class healthSetMealServiceImpl implements healthSetMealService {

    @Autowired
    healthSetMealDao healthSetMealDao;

    @Override
    public List<Setmeal> findAll() {
        return healthSetMealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return healthSetMealDao.findById(id);
    }
}
