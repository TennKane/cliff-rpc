package com.cliff.rpc.serializer;

import com.cliff.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TravisKey
 */
public class SerializerFactory {
   /* *//**
     * new HashMap<String, Serializer>(){}：这是创建了一个匿名内部类，
     * 并使用双大括号初始化语法来初始化 HashMap。
     * 在大括号内部，通过 put 方法向 KEY_SERIALIZER_MAP 中添加了四个键值对：
     *//*
    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){
        {
            put(SerializerKeys.JDK, new JdkSerializer());
            put(SerializerKeys.JSON, new JsonSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
            put(SerializerKeys.HESSIAN, new HessianSerializer());
        }
    };

    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");*/

    static {
        SpiLoader.load(Serializer.class);
    }

    

    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }

}
