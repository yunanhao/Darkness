package com.mona.common.reflect;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LocalHandler implements InvocationHandler {
    private Object real;

    /**
     * @param proxy  Proxy对象实例
     * @param method 被代理的方法
     * @param args   被代理的方法需要传入的参数数组
     * @return 被代理的实体方法产生的返回值
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        System.out.println(Proxy.getInvocationHandler(proxy) == this);
        Object result = method.invoke(real, args);
        System.out.println("after");
        return result;
    }

    @Test
    public void test() {
        MyCallbackImpl callback = new MyCallbackImpl();
        real = callback;
        Callback<String, String> proxyInstance = (Callback) Proxy.newProxyInstance(callback.getClass().getClassLoader(), new Class[]{Callback.class}, this);
        System.out.println(proxyInstance.onCall("1234"));
    }

    String d(String a) {
        System.out.println("before");
        d(a);
        System.out.println("after");
        return d(a);
    }
}
