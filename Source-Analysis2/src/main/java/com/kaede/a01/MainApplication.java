package com.kaede.a01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author kaede
 * @create 2023-02-19
 */

@SpringBootApplication
@EnableAspectJAutoProxy
// @EnableTransactionManagement
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        MyService service = context.getBean(MyService.class);
        service.service();
    }

}
