package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    RoleService roleService;


    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        // 传递当前页，每页显示的记录数，查询条件
        // 响应PageResult，封装总记录数，结果集
        PageResult pageResult = roleService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Role> list=roleService.findAll();
        if(list.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findAllMenu")
    public Result findAllMenu(){
        List<Menu> list =roleService.findAllMenu();
        if(list.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        try {
            roleService.update(role,permissionIds,menuIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        Role role=roleService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,role);
    }

    @RequestMapping("/findPermissionByRoleId")
    public List<Integer> findPermissionByRoleId(Integer id){
        List<Integer> list= roleService.findPermissionByRoleId(id);
        return list;
    }

    @RequestMapping("/findMenuByRoleId")
    public List<Integer>findMenuByRoleId(Integer id){
        List<Integer> list= roleService.findMenuByRoleId(id);
        return list;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        try {
            roleService.add(role,permissionIds,menuIds);
        }catch (Exception e){
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
