package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.healthSetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.time.chrono.JapaneseDate;
import java.util.List;

@RestController
@RequestMapping("/healthsetmeal")
public class healthSetMealController {

    @Autowired
    JedisPool jedisPool;
    @Reference
    private healthSetMealService healthSetMealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> all=null;
        try {
            String setmeal = jedisPool.getResource().get("AllSetmeal");
            if(setmeal==null){
                all = healthSetMealService.findAll();
                String s = JSON.toJSONString(all);
                jedisPool.getResource().setex("AllSetmeal",60*10,s);
            }else {
                all= JSON.parseArray(setmeal, Setmeal.class);
            }
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,all);
    }


    @RequestMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal=null;
        try {
            String setmealFromRedis = jedisPool.getResource().get("Setmeal");
            if(setmealFromRedis==null){
                setmeal = healthSetMealService.findById(id);
                String s = JSON.toJSONString(setmeal);
                jedisPool.getResource().setex("Setmeal",60*10,s);
            }else {
                setmeal= JSON.parseObject(setmealFromRedis, Setmeal.class);
            }
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
        return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
    }
}
