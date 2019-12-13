package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleDao {
   Role findRolesById(Integer id);

    Page<Role> selectByCondition(String queryString);

    List<Role> findAll();

    List<Menu> findAllMenu();

    void deletePAssociationByid(Integer id);

    void deleteMAssociationByid(Integer id);

    void setRoleAndMenu(Map<String, Integer> map);

    void setRoleAndPermission(Map<String, Integer> map);

    void update(Role role);

    Role findById(Integer id);

    List<Integer> findPermissionByRoleId(Integer id);

    List<Integer> findMenuByRoleId(Integer id);

    void add(Role role);

    long findCountFromMById(Integer id);

    long findCountFromPById(Integer id);

    void deleteById(Integer id);
}
