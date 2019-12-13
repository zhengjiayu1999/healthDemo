package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

     void add(Setmeal setmeal, Integer[] checkgroupIds);

     PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Map<String,Object>> findSetmealCount();
}
