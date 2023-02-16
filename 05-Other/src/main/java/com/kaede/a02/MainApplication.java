package com.kaede.a02;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 组件扫描的核心类
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        scanner.scan(MainApplication.class.getPackage().getName());

        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

}
