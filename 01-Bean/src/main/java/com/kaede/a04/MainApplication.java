package com.kaede.a04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class MainApplication {
    public static void main(String[] args) {
        // GenericApplicationContext是一个【干净】的容器
        // 该容器中并没有添加额外的BeanFactory、Bean后置处理器
        GenericApplicationContext context = new GenericApplicationContext();

        // 用原始方法注册三个bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);

        // 不指定名字时，默认以包名+类名添加到容器中
        // 解析@Autowired和@Value的Bean后置处理器
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        // 默认的解析器无法解析值注入(@Value)，此处换为ContextAnnotationAutowireCandidateResolver
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(
            new ContextAnnotationAutowireCandidateResolver());
        // 解析@Resource、@PostConstruct和@PreDestroy的Bean后置处理器
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        // 将解析@ConfigurationPropertiesBinding注解的Bean后置处理器加入至容器
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());

        // 初始化容器
        // 执行beanFactory后置处理器，添加bean后处理器，初始化所有单例
        context.refresh();

        System.out.println(context.getBean(Bean4.class));

        // 销毁容器
        context.close();
    }
}
