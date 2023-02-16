package com.kaede.a03;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.List;

public class MainApplication2 {

    public static void main(String[] args) throws IOException {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();

        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(new SimpleCommandLinePropertySource(
            "--spring.datasource.url=jdbc:mysql://localhost:3306/test",
            "--spring.datasource.username=root",
            "--spring.datasource.password=123456",
            "--spring.datasource.driver-class-name=com.mysql.jdbc.Driver"
        ));
        context.setEnvironment(env);

        context.registerBean("config", Config.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    // 本项目的配置类
    @Configuration
    // @Import(MyImportSelector.class)
    @EnableAutoConfiguration
    static class Config {
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            List<String> names = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
            return names.toArray(new String[0]);
        }
    }

    // 第三方的配置类
    @Configuration
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
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    static class Bean2 {
    }

}
