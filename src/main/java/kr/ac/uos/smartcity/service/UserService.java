package kr.ac.uos.smartcity.service;

import kr.ac.uos.smartcity.model.User;
import kr.ac.uos.smartcity.model.UserListCondition;
import kr.ac.uos.smartcity.repository.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserMapper mapper;

    public UserService(UserMapper mapper) {
        this.mapper = mapper;
    }

    public void add(User user) {
        mapper.add(user);
    }

    public void update(User user) {
        mapper.update(user);
    }

    public void delete(String id) {
        mapper.delete(id);
    }

    public void suspend(String id) {
        mapper.suspend(id);
    }

    public void unsuspend(String id) {
        mapper.unsuspend(id);
    }

    public int listCount(UserListCondition condition) {
        return mapper.listCount(condition);
    }

    public List<User> list(UserListCondition condition) {
        return mapper.list(condition);
    }

    public boolean exists(String id) {
        return mapper.exists(id) != 0;
    }

    public User getUser(String id) {
        return mapper.getUser(id);
    }
}
