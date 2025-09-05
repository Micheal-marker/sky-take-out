package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO 用户登录数据传输对象
     * @return 登录成功的用户信息
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
