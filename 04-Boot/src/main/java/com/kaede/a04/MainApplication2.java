package com.kaede.a04;

import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class MainApplication2 {

    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    // 本项目的配置类
    @Configuration
    @Import(MyImportSelector.class)
    static class Config {
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};
        }
    }

    // 存在Druid依赖
    static class MyCondition1 implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 判断类路径下是否存在某个类
            // 获取关联注解的属性
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
            String className = attributes.get("className").toString();
            Boolean exists = (Boolean) attributes.get("exists");
            boolean present = ClassUtils.isPresent(className, null);
            return  Boolean.TRUE.equals(exists) ? present : !present;
        }
    }
    /* // 不存在Druid依赖
    static class MyCondition2 implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return !ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", null);
        }
    } */

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Conditional(MyCondition1.class)
    @interface ConditionalOnClass {
        boolean exists();
        String className();
    }

    // 第三方的配置类
    @Configuration
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = true)
    static class AutoConfiguration1 {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
    }
    static class Bean1 {
        private String name;
    }

    // 第三方的配置类
    @Configuration
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = false)
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    static class Bean2 {
    }

}
