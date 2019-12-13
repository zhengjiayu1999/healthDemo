package com.itheima.health.dao;

import com.itheima.health.pojo.Setmeal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface healthSetMealDao {
    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
