package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {

    public void add(Member member);
    public Member findByTelephone(String telephone);

    List<Integer> findCountByMonths(List<String> list);


    Integer findMemberCountFromBoy();

    Integer findMemberCountFromGirl();

    Integer findCountBy18();

    Integer findCountBy18To30();

    Integer findCountBy30To45();

    Integer findCountBy45();
}
