package com.cliffi.example.common.service;

import com.cliffi.example.common.model.User;

/**
 * 用户服务
 *
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    default int getNum(){
        return 1;
    }
}
