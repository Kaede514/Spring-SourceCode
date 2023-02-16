package com.kaede.a01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class MainApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainApplication.class);

        // 按工厂的名字取，取出来的结果是产品
        System.out.println(context.getBean("bean1"));
        System.out.println(context.getBean("bean1"));
        System.out.println(context.getBean("bean1"));
        System.out.println(context.getBean(Bean1.class));

        // 获取工厂本身
        // 1.根据类型获取
        System.out.println(context.getBean(Bean1FactoryBean.class));
        // 2.根据名字获取，&+产品名
        System.out.println(context.getBean("&bean1"));

        context.close();
    }

}
