package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuDao menuDao;
    @Override
    public List<Map<String, Object>> getMenu(String username) {
        return menuDao.getMenu(username);
    }

    @Override
    public List<Map<String,Object>> getChildrenMenu(String id) {
        return menuDao.getChildrenMenu(id);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page=menuDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void delete(Integer id) {
       Integer count = menuDao.findCountByMenuId(id);
       if(count>0){
           throw new RuntimeException("当前菜单项被角色引用，不能删除");
       }else {
           menuDao.delete(id);
       }
    }

    @Override
    public void add(Menu menu) {
        if (menu.getParentMenuId()!=null){
            menuDao.add(menu,2);
        }else {
            menuDao.add(menu,1);
        }
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }
}
