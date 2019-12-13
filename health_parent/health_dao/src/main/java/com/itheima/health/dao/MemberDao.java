package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    int findCountByMonths(String s);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);


    Integer findMemberCountFromBoy();

    Integer findMemberCountFromGirl();

    Integer findCountBy18(String ageStr);

    Integer findCountBy18To30(@Param("beginAge") String beginAge,@Param("endAge") String endAge);

    Integer findCountBy30To45(@Param("beginAge") String beginAge,@Param("endAge") String endAge);

    Integer findCountBy45(String ageStr);
}
