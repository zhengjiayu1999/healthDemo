package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        String s = ValidateCodeUtils.generateValidateCode4String(4);
        try {
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,s);
            System.out.println("手机号为"+telephone+"的验证码为:"+s);
        } catch (Exception e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,60,s);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        String s = ValidateCodeUtils.generateValidateCode4String(4);
        try {
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,s);
            System.out.println("手机号为"+telephone+"的验证码为:"+s);
        } catch (Exception e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,60,s);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
