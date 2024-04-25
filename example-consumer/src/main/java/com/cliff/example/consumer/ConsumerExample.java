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
        RpcConfig rpcConfig = ConfigUtil.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
