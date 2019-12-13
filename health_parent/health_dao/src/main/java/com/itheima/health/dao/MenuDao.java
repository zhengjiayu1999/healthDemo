package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuDao {
    List<Map<String,Object>> getMenu(String username);

    List<Map<String,Object>> getChildrenMenu(String id);

    Page<CheckItem> selectByCondition(String queryString);

    Menu findById(Integer id);

    void delete(Integer id);

    void add(@Param("menu") Menu menu,@Param("level") Integer level);

    Integer findCountByMenuId(Integer id);

    void edit(Menu menu);
}
