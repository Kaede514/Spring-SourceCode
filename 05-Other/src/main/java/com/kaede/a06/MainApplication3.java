package com.kaede.a06;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Configuration
public class MainApplication3 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainApplication3.class);

        /* SmsService bean = context.getBean(SmsService.class);
        for (Method method : SmsService.class.getMethods()) {
            if (method.isAnnotationPresent(MyListener.class)) {
                // 作为适配器进行适配
                ApplicationListener listener = event -> {
                    try {
                        // 监听器方法需要的事件类型
                        Class<?> eventType = method.getParameterTypes()[0];
                        if (eventType.isAssignableFrom(event.getClass())) {
                            method.invoke(bean, event);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                context.addApplicationListener(listener);
            }
        } */


        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        public MyEvent(Object source) {
            super(source);
        }
    }

    // SmartInitializingSingleton接口中的方法在所有单例被初始化后回调
    @Bean
    public SmartInitializingSingleton smartInitializingSingleton(ConfigurableApplicationContext context) {
        return () -> {
            for (String name : context.getBeanDefinitionNames()) {
                Object bean = context.getBean(name);
                for (Method method : bean.getClass().getMethods()) {
                    if (method.isAnnotationPresent(MyListener.class)) {
                        context.addApplicationListener(event -> {
                            System.out.println(event);
                            // 监听器方法需要的事件类型
                            Class<?> eventType = method.getParameterTypes()[0];
                            if (eventType.isAssignableFrom(event.getClass())) {
                                try {
                                    method.invoke(bean, event);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        };
    }

    @Component
    static class MyService {
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness() {
            log.info("主线业务");
            // 主线业务完成后需要做一些支线业务
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }

    @Component
    static class SmsService {
        @MyListener
        public void listener(MyEvent myEvent) {
            log.info("发送短信");
        }
    }

    @Component
    static class EmailService {
        @MyListener
        public void listener(MyEvent myEvent) {
            log.info("发送邮件");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyListener {
    }

}