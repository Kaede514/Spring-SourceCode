package com.kaede.a03;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CglibProxyDemo {

    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    // cglib代理是子类型，目标是父类型
    public static void main(String[] param) {
        Target target = new Target();
        // 参数：父类型、MethodInterceptor(决定代理类中方法执行时的行为)
        // MethodInterceptor中的参数：代理对象自身、正在执行的方法、方法执行时的参数、方法对象
        Target proxy = (Target) Enhancer.create(Target.class, (MethodInterceptor) (p, method, args, methodProxy) -> {
            System.out.println("before...");
            // 方式一：用方法反射调用目标
            // Object result = method.invoke(target, args);
            // 方式二：methodProxy可以避免反射调用，效率更高
            // 1.内部没有用反射，需要目标(spring用的方式)
            Object result = methodProxy.invoke(target, args);
            // 2.内部没有用反射，需要代理
            // Object result = methodProxy.invokeSuper(p, args);
            System.out.println("after...");
            return result;
        });

        System.out.println(proxy.getClass());
        proxy.foo();
    }

}