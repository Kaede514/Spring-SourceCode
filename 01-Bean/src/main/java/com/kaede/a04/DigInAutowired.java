package com.kaede.a04;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// AutowiredAnnotationBeanPostProcessor运行分析
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // registerSingleton()提供Bean的名字和实例对象即可注册到容器中
        // 该方法在调用是会认为传入的bean是成品，不会再走创建过程、依赖注入、初始化
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        // @Value的解析器
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // ${}的解析器
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        // 1. 查找哪些属性、方法加了@Autowired, 这称之为InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
        // 执行依赖注入，该方法中完成对@Autowired、@Value注解的解析
        /* processor.postProcessProperties(null, bean1, "bean1");
        System.out.println(bean1); */

        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        // 获取Bean1上加了@Value、@Autowired的成员变量、方法参数信息
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);

        // 2.调用InjectionMetadata来进行依赖注入，注入时按类型查找值
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        // 3.按类型查找值
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        // 参数为成员变量、是否必需
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        // 根据成员变量找要注入的Bean
        Object o = beanFactory.doResolveDependency(dd1, "bean1", null, null);
        System.out.println(o);

        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        /// 参数为方法、第几个参数、是否必需
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), true);
        Object o1 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o1);

        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), true);
        Object o2 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o2);
    }
}
