package com.kaede.a01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Constructor;
import java.util.List;

public class MainApplication2 {

    public static void main(String[] args) throws Exception {
        // 添加application监听器，监听事件
        SpringApplication application = new SpringApplication();
        application.addListeners(e -> System.out.println(e.getClass()));

        // 获取事件发送器实现类名
        List<String> names = SpringFactoriesLoader.loadFactoryNames(
            SpringApplicationRunListener.class, MainApplication2.class.getClassLoader());
        for (String name : names) {
            System.out.println(name);
            Class<?> clazz = Class.forName(name);
            Constructor<?> constructor = clazz.getConstructor(SpringApplication.class, String[].class);
            SpringApplicationRunListener publisher = (SpringApplicationRunListener)
                constructor.newInstance(application, args);

            // 发布事件
            // SpringBoot开始启动
            publisher.starting();
            // 环境信息准备完毕
            publisher.environmentPrepared(new StandardEnvironment());
            // Spring容器创建并调用初始化器之后，发送此事件
            GenericApplicationContext context = new GenericApplicationContext();
            publisher.contextPrepared(context);
            // 所有BeanDefination加载完毕
            publisher.contextLoaded(context);
            // Spring容器初始化完毕(refresh方法调用完毕)
            context.refresh();
            publisher.started(context);
            // SpringBoot启动完成
            publisher.running(context);
            // SpringBoot启动出错
            publisher.failed(context, new RuntimeException("error"));
        }
    }

}
