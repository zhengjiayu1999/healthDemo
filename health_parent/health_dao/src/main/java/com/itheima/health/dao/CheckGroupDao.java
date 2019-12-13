package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 持久层Dao接口
 */
@Repository
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    //我们也可以直接传递2个参数
    //void setCheckGroupAndCheckItem(@Param(value = "checkGroupId") Integer checkGroupId, @Param(value = "checkitemId")Integer checkitemId);


    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void update(CheckGroup checkGroup);

    long findCountFromItemByGroupId(Integer id);

    long findCountFromSetmealByGroupId(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupById(Integer id);
}