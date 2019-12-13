package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface SetmealDao {
    public void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(HashMap<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);

    List<Map<String,Object>> findSetmealCount();

}
