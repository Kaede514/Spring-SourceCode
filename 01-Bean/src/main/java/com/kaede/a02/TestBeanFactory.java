package com.kaede.a02;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.util.Map;

public class TestBeanFactory {

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }

        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }
    }

    interface Inter {}
    static class Bean3 implements Inter {}
    static class Bean4 implements Inter {}

    @Slf4j
    static class Bean1 {
        public Bean1() {
            log.info("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

        /**
         * @Autowired先根据类型匹配再根据名字匹配，匹配会报错，解决方法：
         *  1.使用@Qualifier
         *  2.将变量名inter改为bean3或bean4
         * @Resource中若未指定名字，也是根据成员变量的名字寻找
         */
        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;
        public Inter getInter() {
            return this.bean3;
        }
    }

    @Slf4j
    static class Bean2 {
        public Bean2() {
            log.info("构造 Bean2()");
        }
    }

    public static void main(String[] args) {
        // BeanFactory最重要的实现
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // bean的定义（class、scope、初始化、销毁）
        AbstractBeanDefinition beanDefinition =
            BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        // 注册bean的定义，此处起名为config
        beanFactory.registerBeanDefinition("config", beanDefinition);

        // 给BeanFactory添加一些常用的后置处理器，对BeanFactory的功能进行扩展
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        // 获取所有BeanFactory后置处理器
        // BeanFactory后置处理器主要功能：补充了一些bean定义
        Map<String, BeanFactoryPostProcessor> postProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        postProcessorMap.values().stream().forEach(beanFactoryPostProcessor -> {
            // 执行BeanFactory后置处理器
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        // Bean后置处理器：针对Bean的生命周期（创建、依赖注入、初始化等）的各个阶段提供一些扩展功能，如@Autowired、@Resource等
        // 获取所有Bean后置处理器
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        // 建立Bean后置处理器和BeanFactory之间的联系
        beanPostProcessorMap.values()
            // AnnotationConfigUtils还会给Bean的后置处理器设置比较器，根据getOrder()的返回值排序
            .stream().sorted(beanFactory.getDependencyComparator())
            .forEach(beanPostProcessor -> {
            System.out.println(beanPostProcessor);
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });

        // 预先实例化所有的单例对象，准备好所有的单例
        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // @Autowired依赖注入的功能对于BeanFactory也是扩展功能
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
        System.out.println(beanFactory.getBean(Bean1.class).getInter());
    }


}
