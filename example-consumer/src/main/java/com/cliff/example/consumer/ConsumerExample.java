package com.cliff.example.consumer;

import com.cliff.rpc.config.ConfigUtil;
import com.cliff.rpc.config.RpcConfig;
import com.cliff.rpc.proxy.ServiceProxyFactory;
import com.cliffi.example.common.model.User;
import com.cliffi.example.common.service.UserService;

/**
 * 简易服务消费者示例
 *
 
 */
public class ConsumerExample {

    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("cliff");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
