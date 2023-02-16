package com.kaede.a07;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class MainApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainApplication.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        public MyEvent(Object source) {
            super(source);
        }
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
    static class SmsApplicationListener implements ApplicationListener<MyEvent> {
        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("发送短信");
        }
    }

    @Component
    static class EmailApplicationListener implements ApplicationListener<MyEvent> {
        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("发送邮件");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        return executor;
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext context, ThreadPoolTaskExecutor executor) {
        return new AbstractApplicationEventMulticaster() {
            private List<GenericApplicationListener> listeners = new ArrayList<>();

            // 收集监听器
            @Override
            public void addApplicationListenerBean(String name) {
                ApplicationListener listener = context.getBean(name, ApplicationListener.class);
                System.out.println("listener = " + listener);
                // 获取该监听器支持的事件类型
                ResolvableType type = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric(0);
                System.out.println("type = " + type);

                // 将原始的listener封装为支持事件类型检查的listener
                GenericApplicationListener genericApplicationListener = new GenericApplicationListener() {
                    // 是否支持某真实的事件类型
                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {
                        return type.isAssignableFrom(eventType);
                    }
                    // 调用原始listenr的onApplicationEvent
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        executor.submit(() -> listener.onApplicationEvent(event));
                    }
                };

                listeners.add(genericApplicationListener);
            }

            // 发布事件
            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
                for (GenericApplicationListener listener : listeners) {
                    if (listener.supportsEventType(ResolvableType.forClass(event.getClass()))) {
                        listener.onApplicationEvent(event);
                    }
                }
            }
        };
    }

    abstract static class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {
        @Override
        public void addApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void addApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void removeApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeAllListeners() {

        }

        @Override
        public void multicastEvent(ApplicationEvent event) {

        }

        @Override
        public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

        }
    }

}