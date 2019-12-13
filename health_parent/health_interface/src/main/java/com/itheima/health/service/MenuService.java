package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Map<String,Object>> getMenu(String username);

    List<Map<String,Object>> getChildrenMenu(String id);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Menu findById(Integer id);

    void delete(Integer id);

    void add(Menu menu);

    void edit(Menu menu);
}
