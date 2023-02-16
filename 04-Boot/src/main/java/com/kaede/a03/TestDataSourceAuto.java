package com.kaede.a03;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

public class TestDataSourceAuto {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(new SimpleCommandLinePropertySource(
            "--spring.datasource.url=jdbc:mysql://localhost:3306/test",
            "--spring.datasource.username=root",
            "--spring.datasource.password=123456",
            "--spring.datasource.driver-class-name=com.mysql.jdbc.Driver"
        ));
        context.setEnvironment(env);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
        context.registerBean(Config.class);

        Package packageName = TestDataSourceAuto.class.getPackage();
        // 将SpringBoot引导类的包名记录下来，将来执行扫描的过程中确定扫描范围
        AutoConfigurationPackages.register(context.getDefaultListableBeanFactory(),
            packageName.getName());
        System.out.println("当前包名: " + packageName);

        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            String resourceDescription = context.getBeanDefinition(name).getResourceDescription();
            if (resourceDescription != null)
                System.out.println(name + " 来源:" + resourceDescription);
        }

        /* DataSourceProperties bean = context.getBean(DataSourceProperties.class);
        System.out.println(bean.getUrl());
        System.out.println(bean.getUsername());
        System.out.println(bean.getPassword()); */
    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{
                DataSourceAutoConfiguration.class.getName(),
                MybatisAutoConfiguration.class.getName(),
                DataSourceTransactionManagerAutoConfiguration.class.getName(),
                TransactionAutoConfiguration.class.getName()
            };
        }
    }

}
