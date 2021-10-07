package kr.ac.uos.smartcity.service;

import kr.ac.uos.smartcity.model.LoginHistory;
import kr.ac.uos.smartcity.model.LoginHistoryCondition;
import kr.ac.uos.smartcity.repository.LoginHistoryMapper;
import kr.ac.uos.smartcity.repository.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoginHistoryService {
    private final LoginHistoryMapper mapper;

    public LoginHistoryService(LoginHistoryMapper mapper) {
        this.mapper = mapper;
    }

    public void add(String id, String ip) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setId(id);
        loginHistory.setIp(ip);
        loginHistory.setLoggedIn(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        mapper.add(loginHistory);
    }

    public int listCount(LoginHistoryCondition condition) {
        return mapper.listCount(condition);
    }

    public List<LoginHistory> list(LoginHistoryCondition condition) {
        return mapper.list(condition);
    }
}
