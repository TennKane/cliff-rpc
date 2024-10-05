package com.cliff.rpc.config;

import com.cliff.rpc.loadbalancer.LoadBalancerKeys;
import com.cliff.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @Author: TeenKane
 */
@Data
public class RpcConfig {
    //RPC框架名称
    private String name = "cliff-rpc";
    //RPC框架版本
    private String version = "1.0";
    //服务端Host
    private String serverHost = "localhost";
    //服务端Port
    private Integer serverPort = 8080;
    //接口MOCK
    private boolean mock = false;
    //序列化方式
    private String serializer = SerializerKeys.JDK;
    //注册中心信息
    private RegistryConfig registryConfig = new RegistryConfig();

    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
}
