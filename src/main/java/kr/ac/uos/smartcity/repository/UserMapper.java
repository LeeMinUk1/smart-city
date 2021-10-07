package kr.ac.uos.smartcity.repository;

import kr.ac.uos.smartcity.model.LoginHistory;
import kr.ac.uos.smartcity.model.User;
import kr.ac.uos.smartcity.model.UserListCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void add(User user);

    void update(User user);

    void delete(String id);

    void suspend(String id);

    void unsuspend(String id);

    int listCount(UserListCondition condition);

    List<User> list(UserListCondition condition);

    int exists(String id);

    User getUser(String id);
}
