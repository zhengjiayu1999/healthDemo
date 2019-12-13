package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<User> page=userDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Role> findAll() {
        return userDao.findAll();
    }

    @Override
    public void update(User user, Integer[] roleIds) {
        userDao.deleteAssociationById(user.getId());
        setUserAndRole(user.getId(),roleIds);
        userDao.update(user);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleById(Integer id) {
        return userDao.findRoleById(id);
    }

    @Override
    public void delete(Integer id) {
        long count=userDao.findCountFromURById(id);
        if(count>0){
            throw new RuntimeException("当前用户被角色引用，不能删除");
        }
        userDao.delete(id);
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        userDao.add(user);
        setUserAndRole(user.getId(),roleIds);
    }

    private void setUserAndRole(Integer userid, Integer[] roleIds) {
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("user_id", userid);
                map.put("role_id", roleId);
                userDao.setUserAndRole(map);
            }
        }
    }
}
