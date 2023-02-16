package com.kaede.a03;

import java.io.IOException;
import java.lang.reflect.Proxy;

public class JdkProxyDemo {

    interface Foo {
        void foo();
    }

    static final class Target implements Foo {
        public void foo() {
            System.out.println("target foo");
        }
    }

    // jdk只能针对接口代理
    public static void main(String[] param) throws IOException {
        // 目标对象
        Target target = new Target();
        // 类加载器，用来加载在运行期间动态生成的字节码
        ClassLoader loader = JdkProxyDemo.class.getClassLoader();
        // 参数：类加载器、代理类实现的接口、InvocationHandler(规定接口中方法的行为)
        // InvocationHandler中的参数：代理对象本身、正在执行的方法对象、方法传来的实际参数
        Foo proxy = (Foo) Proxy.newProxyInstance(loader, new Class[]{Foo.class}, (p, method, args) -> {
            System.out.println("before...");
            // 调用目标方法
            Object result = method.invoke(target, args);
            System.out.println("after....");
            // 返回目标方法执行的结果
            return result;
        });

        System.out.println(proxy.getClass());
        proxy.foo();

        // 等待键盘输入
        System.in.read();
    }

}