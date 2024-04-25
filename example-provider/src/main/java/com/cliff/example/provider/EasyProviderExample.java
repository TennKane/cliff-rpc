package com.cliff.example.provider;

import com.cliff.rpc.RpcApplication;
import com.cliffi.example.common.service.UserService;
import com.cliff.rpc.registry.LocalRegistry;
import com.cliff.rpc.server.HttpServer;
import com.cliff.rpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 * @author 关生
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        RpcApplication.init();
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
