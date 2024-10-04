package com.cliff.rpc.registry;

import com.cliff.rpc.config.RegistryConfig;
import com.cliff.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @Author: TeenKane
 */
public interface Registry {
    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 监听（消费端使用）
     */
    void watch(String serviceNodekey);
    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（客户端获取某服务的所有节点
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();



}
