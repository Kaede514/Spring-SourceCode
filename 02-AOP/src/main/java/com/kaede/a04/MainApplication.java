package com.kaede.a04;

import java.lang.reflect.Method;

public class MainApplication {

    interface Foo {
        void foo();
        int bar();
    }

    static class Target implements Foo {
        public void foo() {
            System.out.println("target foo");
        }

        @Override
        public int bar() {
            System.out.println("target bar");
            return 100;
        }
    }

    interface InvocationHandler {
        Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    }

    public static void main(String[] params) {
        Foo proxy = new $Proxy0((p, method, args) -> {
            // 1.功能增强
            System.out.println("before...");
            // 2.调用目标
            return method.invoke(new Target(), args);
        });
        proxy.foo();
        System.out.println(proxy.bar());
    }

}
