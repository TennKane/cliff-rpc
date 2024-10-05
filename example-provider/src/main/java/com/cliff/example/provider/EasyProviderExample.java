package com.cliff.example.provider;

import com.cliff.rpc.RpcApplication;
import com.cliff.rpc.config.RegistryConfig;
import com.cliff.rpc.config.RpcConfig;
import com.cliff.rpc.model.ServiceMetaInfo;
import com.cliff.rpc.registry.Registry;
import com.cliff.rpc.registry.RegistryFactory;
import com.cliff.rpc.server.tcp.VertxTcpServer;
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
        String serviceName = UserService.class.getName();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 启动 web 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
