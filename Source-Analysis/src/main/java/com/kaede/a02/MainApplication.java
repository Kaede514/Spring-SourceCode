package com.kaede.a02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author kaede
 * @create 2023-02-16
 */

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        ConfigurableEnvironment env = context.getEnvironment();
        System.out.println(env.getProperty("user_name"));
    }

}

