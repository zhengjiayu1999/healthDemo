package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public List<Integer> findCountByMonths(List<String> list) {

        List<Integer> memberCount =new ArrayList<>();
        for (String s : list) {
           int number = memberDao.findCountByMonths(s);
           memberCount.add(number);
        }
        return memberCount;
    }

    @Override
    public Integer findMemberCountFromBoy() {
        return memberDao.findMemberCountFromBoy();
    }

    @Override
    public Integer findMemberCountFromGirl() {
        return memberDao.findMemberCountFromGirl();
    }

    @Override
    public Integer findCountBy18() {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,-18);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ageStr = format.format(calendar.getTime());
        return memberDao.findCountBy18(ageStr);
    }

    @Override
    public Integer findCountBy18To30() {
        String[] yearByAge = getYearByAge(18, 30);
        return memberDao.findCountBy18To30(yearByAge[1],yearByAge[0]);
    }

    @Override
    public Integer findCountBy30To45() {
        String[] yearByAge = getYearByAge(30, 45);
        return memberDao.findCountBy30To45(yearByAge[1],yearByAge[0]);
    }

    @Override
    public Integer findCountBy45() {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,-46);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ageStr = format.format(calendar.getTime());
        return memberDao.findCountBy45(ageStr);
    }

    private String[] getYearByAge(int beginAge,int endAge){
        String [] array=new String[2];
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,-beginAge);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(calendar.getTime());
        System.out.println(format1);
        array[0]=format1;
        calendar.add(Calendar.YEAR,beginAge-endAge);
        String format2 = format.format(calendar.getTime());
        System.out.println(format2);
        array[1]=format2;
        return array;
    }

}
