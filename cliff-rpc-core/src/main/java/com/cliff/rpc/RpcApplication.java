package com.cliff.rpc;

import com.cliff.rpc.config.ConfigUtil;
import com.cliff.rpc.config.RegistryConfig;
import com.cliff.rpc.config.RpcConfig;
import com.cliff.rpc.constant.RpcConstant;
import com.cliff.rpc.registry.Registry;
import com.cliff.rpc.registry.RegistryFactory;
import com.cliff.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

/**
 * @Author: TravisKey
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);

        log.info("rpc init config = {}",newRpcConfig.toString());

        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }

        return rpcConfig;
    }

}