package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Reference
    OrderSettingService ordersettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile file){
        ArrayList<OrderSetting> orderSettingList=new ArrayList<>();
        try {
            List<String[]> strings = POIUtils.readExcel(file);
            for (String[] string : strings) {
                OrderSetting orderSetting=new OrderSetting(new Date(string[0]),Integer.parseInt(string[1]));
                orderSettingList.add(orderSetting);
            }
            ordersettingService.add(orderSettingList);
        } catch (IOException e) {
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        List<Map> list =null;
        try {
           list = ordersettingService.getOrderSettingByMonth(date);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
    }

    @RequestMapping("editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            ordersettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
