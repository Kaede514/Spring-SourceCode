package org.springframework.boot;

import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

public class Step5 {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        ConfigurableEnvironment env = new StandardEnvironment();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 增强前");
        env.getPropertySources().forEach(ps -> {
            System.out.println(ps.getName());
        });

        // 补充application.properties源和random源
        ConfigFileApplicationListener postProcessor = new ConfigFileApplicationListener();
        postProcessor.postProcessEnvironment(env, application);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 增强后");
        env.getPropertySources().forEach(ps -> {
            System.out.println(ps.getName());
        });
        System.out.println("server.port = " + env.getProperty("server.port"));
        System.out.println("random.int = " + env.getProperty("random.int"));
        System.out.println("random.int = " + env.getProperty("random.int"));
        System.out.println("random.uuid = " + env.getProperty("random.uuid"));
        System.out.println("random.uuid = " + env.getProperty("random.uuid"));

        // 应该通过读取spring.factories配置文件，而不是硬编码在代码中
        // 由监听器读取EnvironmentPostProcessor，并调用其中的方法
        List<String> names = SpringFactoriesLoader.loadFactoryNames(
            EnvironmentPostProcessor.class, Step5.class.getClassLoader());
        names.forEach(System.out::println);
    }

}
