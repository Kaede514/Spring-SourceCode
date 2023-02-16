package com.kaede.a05;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author kaede
 * @create 2023-02-13
 */

public class TestProxy {

    public static void main(String[] args) throws Exception {
        byte[] dump = $Proxy0Dump.dump();

        /* FileOutputStream fos = new FileOutputStream("$Proxy0.class");
        fos.write(dump, 0, dump.length);
        fos.close(); */

        // 自定义类加载器
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                // 根据字节数组生成类对象
                return super.defineClass(name, dump, 0, dump.length);
            }
        };
        // 传入代理类的类名
        Class<?> proxyClass = classLoader.loadClass("com.kaede.a05.$Proxy0");
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        Foo proxy = (Foo) constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before...");
                System.out.println("调用目标");
                return null;
            }
        });
        proxy.foo();
    }

}
