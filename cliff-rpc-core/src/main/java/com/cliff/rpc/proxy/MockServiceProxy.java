package com.cliff.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: TravisKey
 */
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        return getDefaultObject(returnType);
    }

    public Object getDefaultObject(Class<?> returnType){
        if(returnType.isPrimitive()){
            if(returnType == boolean.class){
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            }
        }
        return null;
    }
}
