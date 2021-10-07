package kr.ac.uos.smartcity.controller;

import kr.ac.uos.smartcity.model.*;
import kr.ac.uos.smartcity.service.LoginHistoryService;
import kr.ac.uos.smartcity.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/login-history")
public class LoginHistoryController {
    private final LoginHistoryService service;

    public LoginHistoryController(LoginHistoryService service) {
        this.service = service;
    }

    @PostMapping("/list")
    public Page<LoginHistory> list(@RequestBody LoginHistoryCondition condition) {
        Page<LoginHistory> page = new Page<>();
        page.setTotalRecords(this.service.listCount(condition));
        page.setData(this.service.list(condition));
        return page;
    }
}
