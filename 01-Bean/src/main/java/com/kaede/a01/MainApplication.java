package com.kaede.a01;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * @author kaede
 * @create 2023-02-11
 */

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {
        // 返回值为Spring容器
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        /**
         * BeanFactory
         * - ApplicationContext的父接口
         * - 是Spring的核心容器，主要的ApplicationContext实现都【借助/组合】了它的功能
         */
        // 实际上调用了BeanFactory的getBean()
        // context.getBean("");
        System.out.println(context);
        /**
         * BeanFactory的作用
         * - 表面上只有getBean等基本方法
         * - 实际上控制反转、基本的依赖注入、直至Bean的生命周期的各种功能, 都由它的实现类提供
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e -> e.getKey().startsWith("component"))
            .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
        /**
         * ApplicationContext比BeanFactory多出的功能
         * - 处理国际化资源
         * - 通配符匹配资源
         * - 读取系统环境变量
         * - 发布事件
         */
        // 根据key找到不同语言版本翻译的结果，默认的翻译信息都存储在以messages_xx开头的文件中
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
        Resource[] resources = context.getResources("classpath:application.yml");
        for (Resource resource : resources) {
            System.out.println(resource);
        }
        Resource[] resources2 = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources2) {
            System.out.println(resource);
        }
        System.out.println(context.getEnvironment().getProperty("java_home"));
        System.out.println(context.getEnvironment().getProperty("server.port"));
        // 发布事件，传入事件源
        // 接收事件需要监听器，在Spring中任何一个组件都可以作为监听器，需要在方法上添加@EventListener
        // 使用事件的好处是解耦
        context.publishEvent(new UserRegisteredEvent(context));
        context.getBean(Component1.class).register();
    }

}
