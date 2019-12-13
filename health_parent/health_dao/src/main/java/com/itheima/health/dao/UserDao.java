package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {
    User findUserByUsername(String username);

    Page<User> selectByCondition(String queryString);

    List<Role> findAll();

    void deleteAssociationById(Integer id);

    void setUserAndRole(Map<String, Integer> map);

    void update(User user);

    User findById(Integer id);

    List<Integer> findRoleById(Integer id);

    long findCountFromURById(Integer id);

    void delete(Integer id);

    void add(User user);
}
