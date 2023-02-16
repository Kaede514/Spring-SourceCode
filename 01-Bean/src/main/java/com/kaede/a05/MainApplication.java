package com.kaede.a05;

import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws IOException {
        // GenericApplicationContext是一个【干净】的容器
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        // 解析@ComponentScan去扫描组件补充到BeanFactory中
        // 解析@Configuration和其中的@Bean将方法根据方法定义补充至BeanFactory
        // 解析@Import、@ImportResource等
        // context.registerBean(ConfigurationClassPostProcessor.class);
        // 与Mybatis整合时用到的类，扫描Mybatis的Mapper接口，将它们也作为beanDefination补充到BeanFactory中
        // @MapperScan底层也是用的MapperScannerConfigurer
        /* context.registerBean(MapperScannerConfigurer.class, bd -> {
            bd.getPropertyValues().add("basePackage", "com.kaede.a05.mapper");
        }); */

        // 获得该类上的@ComponentScan注解
        // context.registerBean(ComponentScanPostProcessor.class);

        context.registerBean(AtBeanPostProcessor.class);

        context.registerBean(MapperPostProcessor.class);

        // 初始化容器
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        // 销毁容器
        context.close();
    }

}
