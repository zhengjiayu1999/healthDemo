package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Role> findAll();

    void update(User user, Integer[] roleIds);

    User findById(Integer id);

    List<Integer> findRoleById(Integer id);

    void delete(Integer id);

    void add(User user, Integer[] roleIds);
}
