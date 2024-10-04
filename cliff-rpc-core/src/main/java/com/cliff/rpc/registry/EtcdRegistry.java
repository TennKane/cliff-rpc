package com.cliff.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.cliff.rpc.config.RegistryConfig;
import com.cliff.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Author: TeenKane
 */
public class EtcdRegistry implements Registry{
    private Client client;

    private KV kvClient;

    /**
     *   根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本机注册的节点key集合（用于维护续期
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 服务端信息本地缓存
     * @param registryConfig
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的key集合
     * @param registryConfig
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        Lease leaseClient = client.getLeaseClient();

        long leaseId = leaseClient.grant(30).get().getID();

        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo),StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();

        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void heartBeat() {
        //hutool提供的定时任务，对所有节点执行重新注册操作
        CronUtil.schedule("*/10*****", new Task() {
            @Override
            public void execute() {
                for(String key : localRegisterNodeKeySet){
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key,StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        if(CollUtil.isEmpty(keyValues)){
                            continue;
                        }
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value,ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                       throw new RuntimeException(key + "续约失败",e);
                    }
                }
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodekey) {
        Watch watchClient = client.getWatchClient();
        boolean newWatch = watchingKeySet.add(serviceNodekey);
        if(newWatch){
            watchClient.watch(ByteSequence.from(serviceNodekey,StandardCharsets.UTF_8),response->{
                for(WatchEvent event : response.getEvents()){
                    switch (event.getEventType()){
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey,StandardCharsets.UTF_8));
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //先从缓存获取服务
        List<ServiceMetaInfo> cachedServiceMetaInfoList = registryServiceCache.readCache();
        if(cachedServiceMetaInfoList != null){
            return cachedServiceMetaInfoList;
        }

        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        //监听key的变化
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value,ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        for(String key : localRegisterNodeKeySet){
            try {
                kvClient.delete(ByteSequence.from(key,StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
    }
}
