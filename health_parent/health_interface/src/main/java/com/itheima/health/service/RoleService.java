package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Role> findAll();

    List<Menu> findAllMenu();

    void update(Role role, Integer[] permissionIds, Integer[] menuIds);

    Role findById(Integer id);

    List<Integer> findPermissionByRoleId(Integer id);

    List<Integer> findMenuByRoleId(Integer id);

    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    void delete(Integer id);
}
