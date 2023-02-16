package com.kaede.a06;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MainApplication {

    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        Target target = new Target();
        proxy.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object p, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before...");
                // 反射调用
                // return method.invoke(target, objects);
                // 没有用反射，结合目标使用(spring用的方式)
                // return methodProxy.invoke(target, objects);
                // 没有用反射，结合代理代理
                return methodProxy.invokeSuper(p, objects);
            }
        });

        proxy.save();
        proxy.save(1);
        proxy.save(2L);
    }

}
