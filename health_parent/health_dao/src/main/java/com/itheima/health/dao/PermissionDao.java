package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao {
   Permission findPermissionMapById(Integer id);

   Page<Permission> selectByCondition(String queryString);

   Permission findById(Integer id);

   Integer findCountByMenuId(Integer id);

   void delete(Integer id);

   void add(Permission permission);

   void edit(Permission permission);
}
