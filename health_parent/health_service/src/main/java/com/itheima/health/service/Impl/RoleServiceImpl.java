package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Role> page = roleDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public List<Menu> findAllMenu() {
        return roleDao.findAllMenu();
    }

    @Override
    public void update(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //根据检查组id删除中间表数据（清理原有关联关系）
        roleDao.deletePAssociationByid(role.getId());
        roleDao.deleteMAssociationByid(role.getId());
        //向中间表插入数据（建立检查组和检查项关联关系）
        setRoleAndPermission(role.getId(),permissionIds);
        setRoleAndMenu(role.getId(),menuIds);
        //更新检查组基本信息
        roleDao.update(role);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionByRoleId(Integer id) {
        return roleDao.findPermissionByRoleId(id);
    }

    @Override
    public List<Integer> findMenuByRoleId(Integer id) {
        return roleDao.findMenuByRoleId(id);
    }

    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.add(role);
        setRoleAndPermission(role.getId(),permissionIds);
        setRoleAndMenu(role.getId(),menuIds);
    }

    @Override
    public void delete(Integer id) {
        long count1=roleDao.findCountFromMById(id);
        long count2=roleDao.findCountFromPById(id);
        if(count1>0) {
            throw new RuntimeException("当前角色被菜单引用，不能删除");
        }else if(count2>0){
            throw new RuntimeException("当前角色被权限引用，不能删除");
        }else{
            roleDao.deleteById(id);
        }
    }

    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if (menuIds != null && menuIds.length > 0) {
            for (Integer menuId : menuIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("role_id", roleId);
                map.put("menu_id", menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

    //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
    public void setRoleAndPermission(Integer roleId,Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("role_id", roleId);
                map.put("permission_id", permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }
}
