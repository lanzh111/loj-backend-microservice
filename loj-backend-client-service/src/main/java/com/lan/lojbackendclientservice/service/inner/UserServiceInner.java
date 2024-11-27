package com.lan.lojbackendclientservice.service.inner;

import com.lan.lojbackendcommonservice.dto.enums.UserRoleEnum;
import com.lan.lojbackendmodelservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务
 *
 */

@RestController
@FeignClient(name = "loj-backend-user-service")
public interface UserServiceInner{

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @GetMapping("/api/user/inner/current")
    User getLoginUser();

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/api/user/inner/get")
    User getById(@RequestParam("id") Long id);


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    @GetMapping("/api/inner/user/admin")
    default boolean isAdmin(User user){
     return  user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



}
