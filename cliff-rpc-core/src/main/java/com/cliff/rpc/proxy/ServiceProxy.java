package com.cliff.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cliff.rpc.RpcApplication;
import com.cliff.rpc.model.RpcRequest;
import com.cliff.rpc.model.RpcResponse;
import com.cliff.rpc.serializer.JdkSerializer;
import com.cliff.rpc.serializer.Serializer;
import com.cliff.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK 动态代理）
 *
 * @author 关生
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            System.out.println("hello");
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8082")
                    .body(bodyBytes)
                    .execute()) {

                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
                return rpcResponse.getData();
                //RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);

               // return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
