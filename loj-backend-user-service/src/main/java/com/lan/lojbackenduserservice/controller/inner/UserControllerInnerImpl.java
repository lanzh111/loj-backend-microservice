package com.lan.lojbackenduserservice.controller.inner;

import com.lan.lojbackendclientservice.service.UserService;
import com.lan.lojbackendclientservice.service.inner.UserServiceInner;
import com.lan.lojbackendmodelservice.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务内部调用实现
 *
 */
@RestController
@RequestMapping("/inner")
public class UserControllerInnerImpl implements UserServiceInner {

    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest httpServletRequest;


    @Override
    @GetMapping("/current")
    public User getLoginUser() {
        return userService.getLoginUser(httpServletRequest);
    }

    @Override
    @GetMapping("/get")
    public User getById(Long id) {
        return userService.getById(id);
    }

}
