package kr.ac.uos.smartcity.repository;

import kr.ac.uos.smartcity.model.LoginHistory;
import kr.ac.uos.smartcity.model.LoginHistoryCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginHistoryMapper {
    void add(LoginHistory loginHistory);

    int listCount(LoginHistoryCondition condition);

    List<LoginHistory> list(LoginHistoryCondition condition);
}
