package com.kaede.a03;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.List;

public class MainApplication {

    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(context.getBean(Bean1.class));
    }

    // 本项目的配置类
    @Configuration
    // 导入其他类
    // @Import({AutoConfiguration1.class, AutoConfiguration2.class})
    @Import(MyImportSelector.class)
    static class Config {
        // @Bean
        public Bean1 bean1() {
            return new Bean1("本项目");
        }
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            // return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            for (String name : SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, null)) {
                System.out.println(name);
            }

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            List<String> names = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
            names.forEach(System.out::println);
            return names.toArray(new String[0]);
        }
    }

    // 第三方的配置类
    @Configuration
    static class AutoConfiguration1 {
        @Bean
        @ConditionalOnMissingBean
        public Bean1 bean1() {
            return new Bean1("第三方");
        }
    }
    @Data
    @AllArgsConstructor
    static class Bean1 {
        private String name;
    }

    // 第三方的配置类
    @Configuration
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    static class Bean2 {
    }

}
