package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @com.alibaba.dubbo.config.annotation.Reference
    UserService userService;

    private User user;

    @RequestMapping("/getName")
    public Result getName(){
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
        return new Result(true,MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());

    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        // 传递当前页，每页显示的记录数，查询条件
        // 响应PageResult，封装总记录数，结果集
        PageResult pageResult = userService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Role> list=userService.findAll();
        if(list.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds){
        try {
            userService.update(user,roleIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        com.itheima.health.pojo.User user=userService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,user);
    }

    @RequestMapping("/findRoleById")
    public List<Integer> findRoleById(Integer id){
        List<Integer> list = userService.findRoleById(id);
        return list;
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            userService.delete(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds){
        try {
            userService.add(user,roleIds);
        }catch (Exception e){
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
