package kr.ac.uos.smartcity.controller;

import kr.ac.uos.smartcity.model.Page;
import kr.ac.uos.smartcity.model.Response;
import kr.ac.uos.smartcity.model.User;
import kr.ac.uos.smartcity.model.UserListCondition;
import kr.ac.uos.smartcity.service.LoginHistoryService;
import kr.ac.uos.smartcity.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final LoginHistoryService loginHistoryService;

    public UserController(UserService service, LoginHistoryService loginHistoryService) {
        this.userService = service;
        this.loginHistoryService = loginHistoryService;
    }

    @PostMapping("/add")
    public Response<Boolean> add(@RequestBody User user) {
        if (this.userService.exists(user.getId())) {
            return new Response<>("같은 ID를 가진 사용자가 이미 존재합니다.");
        }
        this.userService.add(user);
        return new Response<>(true);
    }

    @PostMapping("/update")
    public Response<Boolean> update(@RequestBody User user) {
        this.userService.update(user);
        return new Response<>(true);
    }

    @PostMapping("/delete")
    public Response<Boolean> delete(@RequestParam String id) {
        this.userService.delete(id);
        return new Response<>(true);
    }

    @PostMapping("/suspend")
    public Response<Boolean> suspend(@RequestParam String id) {
        this.userService.suspend(id);
        return new Response<>(true);
    }

    @PostMapping("/unsuspend")
    public Response<Boolean> unsuspend(@RequestParam String id) {
        this.userService.unsuspend(id);
        return new Response<>(true);
    }

    @PostMapping("/list")
    public Page<User> list(@RequestBody UserListCondition condition) {
        Page<User> page = new Page<User>();
        page.setTotalRecords(this.userService.listCount(condition));
        page.setData(this.userService.list(condition));
        return page;
    }

    @PostMapping("/login")
    public Response<User> login(@RequestBody User loginUser) {
        System.out.println(loginUser);
        User user = userService.getUser(loginUser.getId());
        boolean authorized = user != null && user.getPassword().equals(loginUser.getPassword());
        if (authorized) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null) {
                ip = request.getRemoteAddr();
            }

            loginHistoryService.add(loginUser.getId(), ip);
            return new Response<>(user);
        }
        return new Response<>("ID나 비밀번호가 일치하지 않습니다.");
    }
}
