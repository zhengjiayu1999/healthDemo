package com.itheima.health.service;

import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface healthSetMealService {
    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
