package com.cliff.rpc.registry;

import com.cliff.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务本地缓存
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class RegistryServiceCache {
    /**
     * 服务端信息本地缓存
     */
    List<ServiceMetaInfo> serviceCache;

    void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache = newServiceCache;
    }

    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    void clearCache(){
        this.serviceCache = null;
    }
}
