package com.kaede.a03;

import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);

        Bean1 proxy = context.getBean(Bean1.class);

        /**
         * 1.演示spring代理的设计特点
         *   - 依赖注入和初始化影响的是原始对象
         *   - 代理与目标是两个对象，二者成员变量并不共用数据
         */
        System.out.println(">>>>> 代理中的成员变量");
        System.out.println("initialized = " + proxy.initialized);
        System.out.println("bean2 = " + proxy.bean2);
        // Spring容器的单例池中只存代理对象、不存目标对象
        if (proxy instanceof Advised ) {
            Advised advised = (Advised) proxy;
            System.out.println(">>>>> 目标中的成员变量");
            Bean1 target = (Bean1) advised.getTargetSource().getTarget();
            System.out.println("initialized = " + target.initialized);
            System.out.println("bean2 = " + target.bean2);
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>");
        System.out.println(proxy.getBean2());
        System.out.println(proxy.isInitialized());
        // 2.演示static方法、final方法、private方法均无法增强
        proxy.m1();
        proxy.m2();
        proxy.m3();
        Method m4 = Bean1.class.getDeclaredMethod("m1");
        m4.setAccessible(true);
        m4.invoke(proxy);

        context.close();
    }

}
