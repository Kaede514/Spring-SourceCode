package com.kaede.a05;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class MainApplication3 {

    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainApplication3.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        // 当有多个相同类型的bean时，使用@Primary来赋予bean更高的优先级，@Primary会在@Bean解析或组件扫描时被解析
        testPrimary(beanFactory);
        // 最后一道防线是根据成员变量的名字与Bean的名字进行匹配
        testDefault(beanFactory);
    }

    private static void testDefault(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd = new DependencyDescriptor(Target2.class.getDeclaredField("service2"), false);
        Class<?> type = dd.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type)) {
            if (name.equals(dd.getDependencyName())) {
                System.out.println(name);
            }
        }
    }

    private static void testPrimary(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd = new DependencyDescriptor(Target1.class.getDeclaredField("service"), false);
        Class<?> type = dd.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type)) {
            if (beanFactory.getMergedBeanDefinition(name).isPrimary()) {
                System.out.println(name);
            }
        }
    }

    interface Service {
    }
    @Component("service1")
    static class Service1 implements Service {
    }
    @Component("service2")
    @Primary
    static class Service2 implements Service {
    }

    static class Target1 {
        @Autowired
        private Service service;
    }
    static class Target2 {
        @Autowired
        private Service service2;
    }

}
