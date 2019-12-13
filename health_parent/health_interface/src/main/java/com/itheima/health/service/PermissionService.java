package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;

public interface PermissionService {
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Permission findById(Integer id);

    void delete(Integer id);

    void add(Permission permission);

    void edit(Permission permission);
}
