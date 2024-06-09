package com.cliff.rpc.serializer;

import com.cliff.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TravisKey
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();



    

    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }

}
