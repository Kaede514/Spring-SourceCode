package org.springframework.boot;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Step4 {

    public static void main(String[] args) throws IOException {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(
            new ResourcePropertySource("step4", new ClassPathResource("step4.properties")));
        System.out.println(env.getProperty("user.first-name"));
        System.out.println(env.getProperty("user.middle-name"));
        System.out.println(env.getProperty("user.last-name"));

        // 将命名规格为减号分隔
        ConfigurationPropertySources.attach(env);
        env.getPropertySources().forEach(ps -> {
            System.out.println(ps.getName());
        });
        System.out.println(env.getProperty("user.first-name"));
        System.out.println(env.getProperty("user.middle-name"));
        System.out.println(env.getProperty("user.last-name"));
    }

}
