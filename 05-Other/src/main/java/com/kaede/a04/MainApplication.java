package com.kaede.a04;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Configuration
public class MainApplication {

    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainApplication.class);

        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(context.getDefaultListableBeanFactory());

        DependencyDescriptor dd1 = new DependencyDescriptor(
            Bean1.class.getDeclaredField("home"), false);
        test1(dd1, context, resolver);

        DependencyDescriptor dd2 = new DependencyDescriptor(
            Bean1.class.getDeclaredField("age"), false);
        String ageStr = test1(dd2, context, resolver);
        Object age = context.getBeanFactory().getTypeConverter().convertIfNecessary(
            ageStr, dd2.getDependencyType());
        System.out.println(age.getClass());

        DependencyDescriptor dd3 = new DependencyDescriptor(
            Bean2.class.getDeclaredField("bean3"), false);
        test2(dd3, context, resolver);

        DependencyDescriptor dd4 = new DependencyDescriptor(
            Bean4.class.getDeclaredField("value"), false);
        test2(dd4, context, resolver);
    }

    private static String test1(DependencyDescriptor dd1, AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        // 获取@Value的内容
        String value = resolver.getSuggestedValue(dd1).toString();
        System.out.println(value);
        // 解析${}
        value = context.getEnvironment().resolvePlaceholders(value);
        System.out.println(value);
        System.out.println(value.getClass());
        return value;
    }

    private static void test2(DependencyDescriptor dd1, AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        // 获取@Value的内容
        String value = resolver.getSuggestedValue(dd1).toString();
        System.out.println(value);
        // 解析${}
        value = context.getEnvironment().resolvePlaceholders(value);
        System.out.println(value);
        System.out.println(value.getClass());
        // 解析#{}
        BeanExpressionResolver resolver1 = context.getBeanFactory().getBeanExpressionResolver();
        Object obj = resolver1.evaluate(value, new BeanExpressionContext(context.getBeanFactory(), null));
        // 类型转换
        obj = context.getBeanFactory().getTypeConverter().convertIfNecessary(obj, dd1.getDependencyType());
        System.out.println(obj);
        System.out.println(obj.getClass());
    }

    public class Bean1 {
        @Value("${JAVA_HOME}")
        private String home;
        @Value("18")
        private int age;
    }

    public class Bean2 {
        // 配合SpringEL表达式，可做类似于@Autowired的注入
        // #{}中书写EL表达式，@表示根据Bean的名字找
        @Value("#{@bean3}")
        private Bean3 bean3;
    }

    @Component("bean3")
    public class Bean3 {
    }

    static class Bean4 {
        @Value("#{'hello, ' + '${JAVA_HOME}'}")
        private String value;
    }

}
