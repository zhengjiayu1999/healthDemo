package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    JedisPool jedisPool;


    @RequestMapping("/check")
    public Result check(@RequestBody Map map,HttpServletResponse response){
        String telephone=(String)map.get("telephone");
        String validateCode=(String)map.get("validateCode");
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode==null||!(code.equals(validateCode))){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Member member = memberService.findByTelephone(telephone);
        if(member!=null){
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
        }else {
            Member m =new Member();
            m.setPhoneNumber(telephone);
            m.setRegTime(new Date());
            memberService.add(m);
        }
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
