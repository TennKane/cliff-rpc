package com.cliff.rpc.registry;

import com.cliff.rpc.spi.SpiLoader;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
